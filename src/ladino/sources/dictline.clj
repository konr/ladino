(ns ladino.sources.dictline
  (:require [schema.core   :as s]
            [schema.macros :as sm]
            [ladino.schemata :as ls]
            [clojure.string :as str]
            [swiss-arrows.core :refer :all]
            [ladino.utils   :refer :all]
            [midje.sweet    :refer :all]))

(def file "dictline.lat")

(sm/defn first-pass :- [s/String]
  [line :- s/String]
  {:kind (-> line (.substring 76 82) .trim)
   :description (-> line (.substring 110) .trim)
   :words (re-seq #"\w+" (.substring line 0 110))})

(sm/defn count-endings []
  (->> (resource->lines file)
       (map first-pass)
       (group-by :kind)
       (map-vals (partial map (comp count :words)))
       (map-vals set)))

; => {"PRON" #{11}, "NUM" #{11 14}, "ADJ" #{10 11 13}, "CONJ" #{7}, "PREP" #{8}, "N" #{11 12}, "INTERJ" #{7}, "ADV" #{8 10}, "V" #{10 13}, "PACK" #{11}}
