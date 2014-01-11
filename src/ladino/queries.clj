(ns ladino.queries
  (:require [midje.sweet    :refer :all]
            [swiss-arrows.core :refer :all]
            [ladino.peer :as lp]
            [schema.core :as s]
            [schema.macros :as sm]
            [datomic.api :as d]))




(defn matches [attributes & [symbol symbol']]
  (let [symbol (or symbol '?a)
        symbol' (or symbol' '?b)]
    (apply concat
           (for [att attributes]
             (let [new (symbol (str "?" "abc" #_(d/squuid)))]
               [[symbol att new] [symbol' att new]])))))



(defn find-matches [part {:keys [stem ending] :as obj}]
  (case part
    "ADJ" 42
    "V" "Verbo"
    "N" "Nuno"
    #_(let [query {:find '[?a] :in '[$ ?stem ?ending]
                   :where (concat (matches [:part-of-speech])
                                  '[[?a :ending ?ending]
                                    [?b :stem ?stem]])}]
        (-<>> (lp/q query stem ending)


              ))
    ))


(defn rules []
  '[[(same-part-of-speech ?a ?b ?pos)
     [?a :part-of-speech ?pos]
     [?b :part-of-speech ?pos]]
    ])

(sm/defn possible-parts-of-speech
  [{:keys [stem ending]}]
  (let [query {:find '[?e] :in '[$ % ?stem ?ending]
               :where '[[?e :ending ?ending]
                        [?s :stem ?stem]
                        (same-part-of-speech ?e ?s ?pos)]}]
    (-<>> (lp/q query (rules) stem ending) (map first) set)))


(defn match [{:keys [stem ending] :as obj}]
  (for [part (possible-parts-of-speech obj)]
    (find-matches part obj)))
