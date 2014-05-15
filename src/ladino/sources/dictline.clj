(ns ladino.sources.dictline
  (:require [schema.core   :as s]
            [schema.macros :as sm]
            [ladino.schemata :as ls]
            [clojure.string :as str]
            [datomic.api :as d :refer [q]]
            [ladino.models.enums :as enums]
            [swiss-arrows.core :refer :all]
            [ladino.utils   :refer :all]
            [midje.sweet    :refer :all]))

(def file "dictline.lat")




(defmulti parse-token (fn [k v] k))


(defmethod parse-token :default [k v] v)
(defmethod parse-token :part-of-speech [k v] v)


;; ----------------


(def rules
  [["INTERJ" [:stem-1 :part-of-speech]]
   ["PRON"   [:stem-1 :stem-2 :part-of-speech :declension :variant :kind]]
   ["PACK"   [:stem-1 :stem-2 :part-of-speech :declension :variant :kind]]
   ["CONJ"   [:stem-1 :part-of-speech]]
   ["PREP"   [:stem-1 :part-of-speech :case]]
   ["NUM"    [:stem-1                         :part-of-speech :declension :variant :kind :amount]]
   ["NUM"    [:stem-1 :stem-2 :stem-3 :stem-4 :part-of-speech :declension :variant :kind :amount]]
   ["ADJ"    [:stem-1                         :part-of-speech :declension :variant :degree]]
   ["ADJ"    [:stem-1 :stem-2                 :part-of-speech :declension :variant :degree]]
   ["ADJ"    [:stem-1 :stem-2 :stem-3 :stem-4 :part-of-speech :declension :variant :degree]]
   ["ADV"    [:stem-1                 :part-of-speech :degree]]
   ["ADV"    [:stem-1 :stem-2 :stem-3 :part-of-speech :degree]]
   ["N"      [:stem-1         :part-of-speech :declension :variant :gender :number]]
   ["N"      [:stem-1 :stem-2 :part-of-speech :declension :variant :gender :number]]
   ["V"      [:stem-1                         :part-of-speech :declension :variant :transitivity]]
   ["V"      [:stem-1 :stem-2 :stem-3 :stem-4 :part-of-speech :declension :variant :transitivity]]])

(defn parse-terms [map list]
  (-<> list (zipmap (:specific map))
       (map-vals* parse-token <>)
       (conj map) (dissoc :specific)))

(sm/defn second-parse :- {s/Keyword s/Any}
  [{:keys [specific part-of-speech] :as entry}]
  ;; FIX use datalog instead :D
  (->> rules (find-first (fn [[pos stems]] (and (= pos part-of-speech) (= (count specific) (count stems)))))
       or-die second (parse-terms entry)))

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
      :specific (re-seq #"\w+" stuff)})))


(sm/defn parse-file []
  (->> (resource->lines file)
       (map-indexed first-parse)
       (map second-parse)))
