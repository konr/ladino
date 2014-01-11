(ns ladino.sources.endings
  (:require [schema.core   :as s]
            [schema.macros :as sm]
            [ladino.schemata :as ls]
            [clojure.string :as str]
            [swiss-arrows.core :refer :all]
            [ladino.utils   :refer :all]
            [midje.sweet    :refer :all]))

(def endings-file "inflects.lat")

(defmulti process-line first)

(defmethod process-line "INTERJ" [elements]
  (zipmap [:part-of-speech :declension :variant :age :frequency] elements))

(defmethod process-line "ADV" [elements]
  (zipmap [:part-of-speech :degree :declension :variant :age :frequency] elements))

(defmethod process-line "PREP" [elements]
  (zipmap [:part-of-speech :case :declension :variant :age :frequency] elements))

(defmethod process-line "CONJ" [elements]
  (zipmap [:part-of-speech :declension :variant :age :frequency] elements))


(defmethod process-line "N" [elements]
  (zipmap (case (count elements)
            10 [:part-of-speech :declension :variant :case :number :gender :key :size         :age :frequency]
            11 [:part-of-speech :declension :variant :case :number :gender :key :size :ending :age :frequency])
          elements))

(defmethod process-line "ADJ" [elements]
  (zipmap (case (count elements)
            11 [:part-of-speech :declension :variant :case :number :gender :degree :key :size         :age :frequency]
            12 [:part-of-speech :declension :variant :case :number :gender :degree :key :size :ending :age :frequency])
          elements))

(defmethod process-line "V" [elements]
  (zipmap (case (count elements)
            12 [:part-of-speech :declension :variant :tense :voice :mood :person :number :key :size         :age :frequency]
            13 [:part-of-speech :declension :variant :tense :voice :mood :person :number :key :size :ending :age :frequency])
          elements))

(defmethod process-line "VPAR" [elements]
  (zipmap [:part-of-speech :declension :variant :case :number :gender :tense :voice :mood :key :size :ending :age :frequency]
          elements))

(defmethod process-line "PRON" [elements]
  (zipmap (case (count elements)
            10 [:part-of-speech :declension :variant :case :number :gender :key :size         :age :frequency]
            11 [:part-of-speech :declension :variant :case :number :gender :key :size :ending :age :frequency])
          elements))

(defmethod process-line "SUPINE" [elements]
  (zipmap [:part-of-speech :declension :variant :case :number :gender :key :variant :ending :age :frequency]
          elements))

(defmethod process-line "NUM" [elements]
  (zipmap (case (count elements)
            11 [:part-of-speech :declension :variant :case :number :gender :numeral-type :key :size         :age :frequency]
            12 [:part-of-speech :declension :variant :case :number :gender :numeral-type :key :size :ending :age :frequency])
          elements))


(sm/defn parse-line :- [s/String]
  [line :- s/String]
  (let [remove-comments (fn [prefix x] (str/replace x (re-pattern (str prefix ".*")) "")) ;; TODO extract
        remove-ada-comments (partial remove-comments "--")]
    (->> line remove-ada-comments (re-seq #"\w+"))))

(sm/defn read-endings-file []
  (loop [[h & tail :as lines] (resource->lines endings-file)
         edn []]
    (if (empty? lines) edn
        (if-let [parsed-line (-?<> h parse-line process-line)]
          (recur tail (conj edn parsed-line))
          (recur tail edn)))))


