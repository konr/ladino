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
  (-> [:stem-1 :stem-2 :part-of-speech :declension :variant :kind "X" :area "X" "A" :source]
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

(def source
    "X"        "General or unknown or too common to say"
    "A"        ""
    "B"        "C.H.Beeson, A Primer of Medieval Latin, 1925 (Bee)"
    "C"        "Charles Beard, Cassell's Latin Dictionary 1892 (Cas)"
    "D"        "J.N.Adams, Latin Sexual Vocabulary, 1982 (Sex)"
    "E"        "L.F.Stelten, Dictionary of Eccles. Latin, 1995 (Ecc)"
    "F"        "Roy J. Deferrari, Dictionary of St. Thomas Aquinas, 1960 (DeF)"
    "G"        "Gildersleeve + Lodge, Latin Grammar 1895 (G+L)"
    "H"        "Collatinus Dictionary by Yves Ouvrard "
    "I"        "Leverett, F.P., Lexicon of the Latin Language, Boston 1845"
    "J"        "Bracton: De Legibus Et Consuetudinibus Angliæ"
    "K"        "Calepinus Novus, modern Latin, by Guy Licoppe (Cal)"
    "L"        "Lewis, C.S., Elementary Latin Dictionary 1891"
    "M"        "Latham, Revised Medieval Word List, 1980 (Latham)"
    "N"        "Lynn Nelson, Wordlist (Nel)"
    "O"        "Oxford Latin Dictionary, 1982 (OLD)"
    "P"        "Souter, A Glossary of Later Latin to 600 A.D., Oxford 1949 (Souter)"
    "Q"        "Other, cited or unspecified dictionaries"
    "R"        "Plater + White, A Grammar of the Vulgate, Oxford 1926 (Plater)"
    "S"        "Lewis and Short, A Latin Dictionary, 1879 (L+S)"
    "T"        "Found in a translation  --  no dictionary reference"
    "U"        ""            
    "V"        "Vademecum in opus Saxonis - Franz Blatt (Saxo)"
    "W"        "My personal guess, mostly obvious extrapolation (Whitaker or W)   "
    "Y"        "Temp special code"
    "Z"        "Sent by user --  no dictionary reference")

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
