(ns ladino.sources.dictline
  (:require [schema.core   :as s]
            [schema.macros :as sm]
            [ladino.schemata :as ls]
            [clojure.string :as str]
            [swiss-arrows.core :refer :all]
            [ladino.utils   :refer :all]
            [midje.sweet    :refer :all]))

(def file "dictline.lat")


(defmulti second-parse :part-of-speech)


(defmethod second-parse :default [map]
  nil)

(defmethod second-parse "PRON" [{:keys [part-of-speech description words] :as map}]
  (-> [:stem-1 :stem-2 :part-of-speech :declension :variant :kind "X" :area "X" "A" "O"]
      #_(zipmap word)
      words
      ))


(sm/defn first-parse :- [s/String]
  [index :- s/Int
   line  :- s/String]
  {:part-of-speech (-> line (.substring 76 82) .trim)
   :index index
   :description (-> line (.substring 110) .trim)
   :words (re-seq #"\w+" (.substring line 0 110))})


(def areas
  {"X" "All or none"
  "A" "Agriculture, Flora, Fauna, Land, Equipment, Rural"
  "B" "Biological, Medical, Body Parts"
  "D" "Drama, Music, Theater, Art, Painting, Sculpture"
  "E" "Ecclesiastic, Biblical, Religious"
  "G" "Grammar, Retoric, Logic, Literature, Schools"
  "L" "Legal, Government, Tax, Financial, Political, Titles"
  "P" "Poetic"
  "S" "Science, Philosophy, Mathematics, Units/Measures"
  "T" "Technical, Architecture, Topography, Surveying"
  "W" "War, Military, Naval, Ships, Armor"
  "Y" "Mythology"})

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
       (map second-parse)
       (filter identity)
       #_(group-by :kind)
       #_(map-vals (partial map (comp count :words)))
       #_(map-vals set)))

                                        ; => {"PRON" #{11}, "NUM" #{11 14}, "ADJ" #{10 11 13}, "CONJ" #{7}, "PREP" #{8}, "N" #{11 12}, "INTERJ" #{7}, "ADV" #{8 10}, "V" #{10 13}, "PACK" #{11}}
