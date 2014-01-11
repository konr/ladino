(ns ladino.sources.stemlist
  (:require [schema.core   :as s]
            [schema.macros :as sm]
            [ladino.schemata :as ls]
            [clojure.string :as str]
            [swiss-arrows.core :refer :all]
            [ladino.utils   :refer :all]
            [midje.sweet    :refer :all]))

(def stemlist-file "stemlist.lat")

(defmulti process-stemlist-line second)
(defmethod process-stemlist-line "INTERJ" [elements]
  (zipmap [:stem :part-of-speech :declension :key :reference] elements))

(defmethod process-stemlist-line "ADV" [elements]
  (zipmap [:stem :part-of-speech :degree :key :reference] elements))

(defmethod process-stemlist-line "PREP" [elements]
  (zipmap [:stem :part-of-speech :case :key :reference] elements))

(defmethod process-stemlist-line "CONJ" [elements]
  (zipmap [:stem :part-of-speech :key :reference] elements))

(defmethod process-stemlist-line "N" [elements]
  (zipmap [:stem :part-of-speech :declension :variant :gender :number :key :reference] elements))

(defmethod process-stemlist-line "ADJ" [elements]
  (zipmap [:stem :part-of-speech :declension :variant :case :key :reference] elements))

(defmethod process-stemlist-line "V" [elements]
  (zipmap [:stem :part-of-speech :declension :variant :transitivity :key :reference] elements))

(defmethod process-stemlist-line "PRON" [elements]
  (zipmap [:stem :part-of-speech :declension :variant :kind :key :reference] elements))

(defmethod process-stemlist-line "NUM" [elements]
  (zipmap [:stem :part-of-speech :declension :variant :kind :amount :key :reference] elements))

(defmethod process-stemlist-line "PACK" [elements]
  (zipmap [:stem :part-of-speech :declension :variant :kind :key :reference] elements))

(defmethod process-stemlist-line :default [elements]
  (process-stemlist-line (concat [""] elements)))


(defn read-stemlist-file []
  (->> stemlist-file resource->lines
       (map (partial re-seq #"\w+"))
       (map process-stemlist-line)))

