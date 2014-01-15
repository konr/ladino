(ns ladino.utils
  (:require [swiss-arrows.core :refer :all]
            [clojure.string :as str]
            [midje.sweet :refer :all]
            [schema.core :as s]
            [schema.macros :as sm]))

(defmacro facts* [title & forms]
  `(facts ~title
          (s/with-fn-validation
            ~@forms)))

(sm/defn resource->lines :- [s/String]
  [uri :- s/String]
  ((comp str/split-lines slurp clojure.java.io/resource) uri))

(defn map-keys [function map]
  (loop [[[k v] & tail :as all] (vec map) new-map {}]
    (if-not (seq all) new-map (recur tail (assoc new-map (function k) v)))))

(defn map-vals [function map]
  (loop [[[k v] & tail :as all] (vec map) new-map {}]
    (if-not (seq all) new-map (recur tail (assoc new-map k (function v))))))
