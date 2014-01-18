(ns ladino.core
  (:gen-class)
  (:require [schema.core   :as s]
            [schema.macros :as sm]
            [swiss-arrows.core :refer :all]
            [clojure.string :as str]
            [midje.sweet    :refer :all]
            [ladino.schemata :as ls]
            ;; Data sources
            [ladino.sources.stemlist :as lss]
            [ladino.sources.endings  :as lse]
            ;; DB
            [ladino.peer :as lp]
            [ladino.models.extensions :as ext]
            [ladino.models.enums :as enums]
            [ladino.models :as lm]
            ;;
            [ladino.parse :as parse]))



(defn read-sources []
  (doseq [ending (lse/read-endings-file)]
    (lp/transact-one ending))
  (doseq [stem (lss/read-stemlist-file)]
    (lp/transact-one stem)))


(defn init-db []
  (lp/init-db! {:uri (lp/random-uri)
             :extensions [(map #(assoc % :db/id (lp/tempid :db.part/db)) ext/schema-attributes)]
             :seed (map (partial map #(assoc % :db/id (lp/tempid :db.part/db))) enums/all-enums)
             :schemata [(lp/gen-attribute-seq lm/all-attributes)]}))

(defn initialize []
  (init-db)
  (read-sources))


(defn -main [word]
  (println "Initializing...")
  (time (initialize))
  (println (format "Parsing '%s'..." word))
  (time (parse/parse word)))
