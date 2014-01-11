(ns ladino.core
  (:require [schema.core   :as s]
            [schema.macros :as sm]
            [swiss-arrows.core :refer :all]
            [clojure.string :as str]
            [midje.sweet    :refer :all]
            [ladino.schemata :as ls]
            [ladino.peer :as lp]
            [ladino.models :as lm]
            ;; Data sources
            [ladino.sources.stemlist :as lss]
            [ladino.sources.endings  :as lse]))



(defn read-sources []
  (doseq [ending (lse/read-endings-file)]
    (lp/transact-one ending))
  (doseq [stem (lss/read-stemlist-file)]
    (lp/transact-one stem)))


(defn init-db []
  (lp/init-db! {:uri (lp/random-uri) :seed [] :schemata [(lp/gen-attribute-seq lm/all-attributes)]}))

(defn initialize []
  (init-db)
  (read-sources))
