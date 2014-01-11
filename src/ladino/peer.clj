(ns ladino.peer
  (:require [swiss-arrows.core :refer :all]
            [com.stuartsierra.component :as component]
            [ladino.schemata :as ls]
            [schema.core :as s]
            [schema.macros :as sm]
            [datomic.api :as d]))


;;;;;;;;;;
;; CORE ;;
;;;;;;;;;;

(defonce database nil)

(defrecord Database [uri conn seed schemata]
  component/Lifecycle

  (start [component]
    (d/create-database uri)
    (let [conn (d/connect uri)
          db   (d/db conn)]
      (assert conn) (assert db)
      (doseq [kind [schemata seed]]
        (doseq [group kind]
          @(d/transact conn group)))
      (-> component
          (assoc :uri   uri)
          (assoc :conn  conn))))
  (stop [component]))


(defn db [] (d/db (:conn database)))

(sm/defn ^:always-validate init-db!
  [data :- {:uri s/String
            :seed     [[ls/Entity]]
            :schemata [[ls/Entity]]}]
  (->> data map->Database
       .start
       (constantly)
       (alter-var-root #'database)))

(sm/defn random-uri :- s/String []
  (str "datomic:mem://" (d/squuid)))


(sm/defn tempid :- ls/Eid
  ([] (tempid :db.part/user))
  ([partition :- s/Keyword] (d/tempid partition)))


(sm/defn resolve-tx :- ls/Eid
  [tx     :- ls/TxResults
   tempid :- ls/Eid]
  (d/resolve-tempid (db) (:tempids tx) tempid))

;;;;;;;;;;;;;;;;;;;;;;;
;; Schema generation ;;
;;;;;;;;;;;;;;;;;;;;;;;

(sm/defn gen-attribute :- ls/Entity
  [ident :- s/Keyword
   map   :- ls/Entity]
  (assert (keyword? ident))
  (conj
   {:db/id (tempid :db.part/db)
    :db/ident ident
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc (str ident)
    :db.install/_attribute :db.part/db}
   map))

(sm/defn gen-attribute-seq :- [ls/Entity]
  [skeleton :- {s/Keyword ls/Entity}]
  (map (partial apply gen-attribute) skeleton))


;;;;;;;;;;;;;;;;
;; Operations ;;
;;;;;;;;;;;;;;;;

(sm/defn eid->entity :- ls/Entity
  [eid :- ls/Eid]
  (->> eid (d/entity (db))
       seq (into {})))

(sm/defn q :- ls/ResultSet
  [query :- ls/Query, & args]
  (apply d/q query (db) args))

(sm/defn transact :- ls/TxResults
  [data :- [ls/Entity]]
  (->> data (d/transact (:conn database)) deref))

(sm/defn transact-one :- ls/Eid
  [entity :- ls/Entity]
  (let [tempid (tempid)]
    (-> entity (assoc :db/id tempid)
        vector transact (resolve-tx tempid))))

