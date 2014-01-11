(ns ladino.peer
  (:require [swiss-arrows.core :refer :all]
            [com.stuartsierra.component :as component]
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


(sm/defn ^:always-validate init-db!
  [data :- {:uri s/String
            :seed     [[{s/Keyword s/Any}]]
            :schemata [[{s/Keyword s/Any}]]}]
  (->> data map->Database
       .start
       (constantly)
       (alter-var-root #'database)))

(sm/defn random-uri :- s/String []
  (str "datomic:mem://" (d/squuid)))


(defn tempid
  ([] (tempid :db.part/user))
  ([partition] (d/tempid partition)))


;;;;;;;;;;;;;;;;;;;;;;;
;; Schema generation ;;
;;;;;;;;;;;;;;;;;;;;;;;


(defn gen-attribute [ident map]
  (assert (keyword? ident))
  (conj
   {:db/id (tempid :db.part/db)
    :db/ident ident
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc (str ident)
    :db.install/_attribute :db.part/db}
   map))

(defn gen-datomic-attribute-seq [skeleton]
  (map (partial apply gen-attribute) skeleton))


;;;;;;;;;;;;
;; Models ;;
;;;;;;;;;;;;

(def inflects-model
  {:part-of-speech      {}                        
   :declension          {}                    
   :variant             {}                 
   :case                {}              
   :number              {}                
   :gender              {}                
   :degree              {}                
   :key                 {}             
   :size                {}              
   :ending              {}                
   :age                 {}             
   :frequency           {}                   
   :tense               {}               
   :voice               {}               
   :mood                {}              
   :person              {}                
   :numeral-type        {}})



(defn ♥ []
  (init-db! {:uri (random-uri) :seed [] :schemata [(gen-datomic-attribute-seq inflects-model)]}))

