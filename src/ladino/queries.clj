(ns ladino.queries
  (:require [midje.sweet    :refer :all]
            [swiss-arrows.core :refer :all]
            [ladino.peer :as lp]
            [schema.core :as s]
            [schema.macros :as sm]
            [datomic.api :as d]))



(sm/defn match
  [{:keys [stem ending]}]
  (let [query {:find '[?s] :in '[$ ?stem ?ending]
               :where '[[?e :ending ?ending]
                        [?e :part-of-speech ?pos]
                        [?s :part-of-speech ?pos]
                        [?s :stem ?stem]]}]
    (-<>> (lp/q query stem ending)
          (map first)
          (map lp/eid->entity)
          clojure.pprint/pprint)))
