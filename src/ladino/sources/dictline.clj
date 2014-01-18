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

(defmethod second-parse "INTERJ" [{:keys [part-of-speech description pos-specific] :as map}]
  (-> [:stem-1 :part-of-speech]
      (zipmap pos-specific)
      (conj map) (dissoc :pos-specific)))

(defmethod second-parse "PRON" [{:keys [part-of-speech description pos-specific] :as map}]
  (-> [:stem-1 :stem-2 :part-of-speech :declension :variant :kind]
      (zipmap pos-specific)
      (conj map) (dissoc :pos-specific)))

(defmethod second-parse "CONJ" [{:keys [part-of-speech description pos-specific] :as map}]
  (-> [:stem-1 :part-of-speech]
      (zipmap pos-specific)
      (conj map) (dissoc :pos-specific)))

(defmethod second-parse "PREP" [{:keys [part-of-speech description pos-specific] :as map}]
  (-> [:stem-1 :part-of-speech :case]
      (zipmap pos-specific)
      (conj map) (dissoc :pos-specific)))

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


(sm/defn first-parse :- [s/String]
  [index :- s/Int
   line  :- s/String]
  (let [stuff (.substring line 0 100)
        pos   (.substring line 76 82)
        flags (.substring line 100 109)
        descr (.substring line 110)]
    (merge
     (zipmap [:age :area :geo :frequency :source] (re-seq #"\w+" flags))
     {:part-of-speech (.trim pos)
      :description (.trim descr)
      :index index
      :pos-specific (re-seq #"\w+" stuff)})))

(def age
  {"X"                 "--  In use throughout the ages/unknown -- the default"
   "A"     "archaic     --  Very early forms, obsolete by classical times"
   "B"     "early       --  Early Latin, pre-classical, used for effect/poetry"
   "C"     "classical   --  Limited to classical (~150 BC - 200 AD)"
   "D"     "late        --  Late, post-classical (3rd-5th centuries)"
   "E"     "later       --  Latin not in use in Classical times (6-10) Christian"
   "F"     "medieval    --  Medieval (11th-15th centuries)"
   "G"     "scholar     --  Latin post 15th - Scholarly/Scientific   (16-18)"
   "H"     "modern      --  Coined recently, words for new things (19-20)"})

(def frequency
  {"X"                  "--  Unknown or unspecified"
   "A"      "very freq   --  Very frequent, in all Elementry Latin books, top 1000+ words"
   "B"      "frequent    --  Frequent, next 2000+ words"
   "C"      "common      --  For Dictionary, in top 10,000 words"
   "D"      "lesser      --  For Dictionary, in top 20,000 words"
   "E"      "uncommon    --  2 or 3 citations"
   "F"      "very rare   --  Having only single citation in OLD or L+S"
   "I"      "inscription --  Only citation is inscription"
   "M"      "graffiti    --  Presently not much used"
   "N"      "Pliny       --  Things that appear only in Pliny Natural History"})

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
  {"X" "General or unknown or too common to say"
   "A" ""
   "B" "C.H.Beeson, A Primer of Medieval Latin, 1925 (Bee)"
   "C" "Charles Beard, Cassell's Latin Dictionary 1892 (Cas)"
   "D" "J.N.Adams, Latin Sexual Vocabulary, 1982 (Sex)"
   "E" "L.F.Stelten, Dictionary of Eccles. Latin, 1995 (Ecc)"
   "F" "Roy J. Deferrari, Dictionary of St. Thomas Aquinas, 1960 (DeF)"
   "G" "Gildersleeve + Lodge, Latin Grammar 1895 (G+L)"
   "H" "Collatinus Dictionary by Yves Ouvrard "
   "I" "Leverett, F.P., Lexicon of the Latin Language, Boston 1845"
   "J" "Bracton: De Legibus Et Consuetudinibus Angliæ"
   "K" "Calepinus Novus, modern Latin, by Guy Licoppe (Cal)"
   "L" "Lewis, C.S., Elementary Latin Dictionary 1891"
   "M" "Latham, Revised Medieval Word List, 1980 (Latham)"
   "N" "Lynn Nelson, Wordlist (Nel)"
   "O" "Oxford Latin Dictionary, 1982 (OLD)"
   "P" "Souter, A Glossary of Later Latin to 600 A.D., Oxford 1949 (Souter)"
   "Q" "Other, cited or unspecified dictionaries"
   "R" "Plater + White, A Grammar of the Vulgate, Oxford 1926 (Plater)"
   "S" "Lewis and Short, A Latin Dictionary, 1879 (L+S)"
   "T" "Found in a translation  --  no dictionary reference"
   "U" ""
   "V" "Vademecum in opus Saxonis - Franz Blatt (Saxo)"
   "W" "My personal guess, mostly obvious extrapolation (Whitaker or W)   "
   "Y" "Temp special code"
   "Z" "Sent by user --  no dictionary reference"})

(def geo
  {"X" "All or none"
   "A" "Africa"
   "B" "Britian"
   "C" "China"
   "D" "Scandinavia"
   "E" "Egypt"
   "F" "France, Gaul"
   "G" "Germany"
   "H" "Greece"
   "I" "Italy, Rome"
   "J" "India"
   "K" "Balkans"
   "N" "Netherlands"
   "P" "Persia"
   "Q" "Near East"
   "R" "Russia"
   "S" "Spain, Iberia"
   "U" "Eastern Europe"})

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
  (->> #_(resource->lines file)
       #_(map-indexed first-parse)
       ♥♥
       (filter #(= (:part-of-speech %) "ADJ"))
       (map second-parse)
       last
       clojure.pprint/pprint
       #_(filter identity)
       #_(group-by :kind)
       #_(map-vals (partial map (comp count :pos-specific)))
       #_(map-vals set)))

;; {
;; "N" #{11 12} ; 6 7
;; "ADV" #{8 10} ; 3 5
;; "V" #{10 13} ; 5 8
;; "PACK" #{11}} ; 6
