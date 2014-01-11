(ns ladino.utils
  (:require [swiss-arrows.core :refer :all]
            [clojure.string :as str]
            [schema.core :as s]
            [schema.macros :as sm]))

(defmacro facts* [title & forms]
  `(facts ~title
          (s/with-fn-validation
            ~@forms)))

(sm/defn resource->lines :- [s/String]
  [uri :- s/String]
  ((comp str/split-lines slurp clojure.java.io/resource) uri))
