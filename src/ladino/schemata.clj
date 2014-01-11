(ns ladino.schemata
  (:require [schema.core :as s]
            [schema.macros :as sm]))


(defn from-class [class]
  (s/pred #(= (type %) class)))


;;;;;;;;;;;;;
;; Datomic ;;
;;;;;;;;;;;;;

(def Eid (from-class Long))
(def Database (from-class datomic.db.Db))

(def TxResults
  {:db-before s/Any
   :db-after  s/Any
   :tx-data   s/Any
   :tempids   s/Any})

(def Entity
  {s/Keyword s/Any})

