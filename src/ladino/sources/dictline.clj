(ns ladino.sources.dictline
  (:require [schema.core   :as s]
            [schema.macros :as sm]
            [ladino.schemata :as ls]
            [clojure.string :as str]
            [ladino.models.enums :as enums]
            [swiss-arrows.core :refer :all]
            [ladino.utils   :refer :all]
            [midje.sweet    :refer :all]))

(def file "dictline.lat")




(defmulti parse-token (fn [k v] k))


(defmethod parse-token :default [k v] v)
(defmethod parse-token :part-of-speech [k v] v)


;; ----------------


(defn parse-terms [list map]
  (-<> list (zipmap (:pos-specific map))
      (map-vals* parse-token <>)
      (conj map) (dissoc :pos-specific)))

(defmulti second-parse :part-of-speech)

(defmethod second-parse :default [map]
  (throw (Exception. "Error parsing file")))

(defmethod second-parse "INTERJ" [map]
  (parse-terms [:stem-1 :part-of-speech] map))

(defmethod second-parse "PRON" [map]
  (parse-terms [:stem-1 :stem-2 :part-of-speech :declension :variant :kind] map))

(defmethod second-parse "PACK" [map]
  (parse-terms [:stem-1 :stem-2 :part-of-speech :declension :variant :kind] map))

(defmethod second-parse "CONJ" [map]
  (parse-terms [:stem-1 :part-of-speech] map))

(defmethod second-parse "PREP" [map]
  (parse-terms [:stem-1 :part-of-speech :case] map))

(defmethod second-parse "NUM" [{:keys [part-of-speech description pos-specific] :as map}]
  (-> (count pos-specific)
      (case 6 [:stem-1                         :part-of-speech :declension :variant :kind :amount]
            9 [:stem-1 :stem-2 :stem-3 :stem-4 :part-of-speech :declension :variant :kind :amount])
      (zipmap pos-specific)
      (conj map) (dissoc :pos-specific)))

(defmethod second-parse "ADJ" [{:keys [part-of-speech description pos-specific] :as map}]
  (-> (count pos-specific)
      (case 5 [:stem-1                         :part-of-speech :declension :variant :degree]
            6 [:stem-1 :stem-2                 :part-of-speech :declension :variant :degree]
            8 [:stem-1 :stem-2 :stem-3 :stem-4 :part-of-speech :declension :variant :degree])
      (zipmap pos-specific)
      (conj map) (dissoc :pos-specific)))

(defmethod second-parse "ADV" [{:keys [part-of-speech description pos-specific] :as map}]
  (-> (count pos-specific)
      (case 3 [:stem-1                 :part-of-speech :degree]
            5 [:stem-1 :stem-2 :stem-3 :part-of-speech :degree])
      (zipmap pos-specific)
      (conj map) (dissoc :pos-specific)))

(defmethod second-parse "N" [{:keys [part-of-speech description pos-specific] :as map}]
  (-> (count pos-specific)
      (case 6 [:stem-1         :part-of-speech :declension :variant :gender :number]
            7 [:stem-1 :stem-2 :part-of-speech :declension :variant :gender :number])
      (zipmap pos-specific)
      (conj map) (dissoc :pos-specific)))

(defmethod second-parse "V" [{:keys [part-of-speech description pos-specific] :as map}]
  (-> (count pos-specific)
      (case 5 [:stem-1                         :part-of-speech :declension :variant :transitivity]
            8 [:stem-1 :stem-2 :stem-3 :stem-4 :part-of-speech :declension :variant :transitivity])
      (zipmap pos-specific)
      (conj map) (dissoc :pos-specific)))

(sm/defn first-parse :- [s/String]
  [index :- s/Int
   line  :- s/String]
  (let [stuff (.substring line 0 100)
        pos   (.substring line 76 82)
        flags (.substring line 100 109)
        descr (.substring line 110)
        age-map    (enums/build enums/age)
        freq-map   (enums/build enums/frequency)
        area-map   (enums/build enums/areas)
        source-map (enums/build enums/sources)
        geo-map    (enums/build enums/geo)]
    (merge
     (->> (re-seq #"\w+" flags)
          (map #(%1 %2) [age-map area-map geo-map freq-map source-map])
          (zipmap [:whitaker/age :whitaker/area :whitaker/geo :whitaker/frequency :whitaker/source]))
     {:part-of-speech (.trim pos)
      :description (.trim descr)
      :index (inc index) ; zero-indexed
      :pos-specific (re-seq #"\w+" stuff)})))


;; function NUMBER_OF_STEMS(P : PART_OF_SPEECH_TYPE) return STEM_KEY_TYPE is
;; begin
;;   case P is
;;     when N       => return 2;
;;     when PRON    => return 2;
;;     when PACK    => return 2;
;;     when ADJ     => return 4;
;;     when NUM     => return 4;
;;     when ADV     => return 3;
;;     when V       => return 4;
;;     when VPAR    => return 0;
;;     when SUPINE  => return 0;
;;     when PREP    => return 1;
;;     when CONJ    => return 1;
;;     when INTERJ  => return 1;
;;     when others  => return 0;
;;   end case;
;; end NUMBER_OF_STEMS;


(sm/defn parse-file []
  (->> (resource->lines file)
       (map-indexed first-parse)
       (map second-parse)))
