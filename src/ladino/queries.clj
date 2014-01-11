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

(def rules
  '[[(same-part-of-speech ?a ?b ?pos)
     [?a :part-of-speech ?pos]
     [?b :part-of-speech ?pos]]
    [(has-equal ?a ?b ?key)
     [?a ?key ?x]
     [?b ?key ?x]]
    [(consistent ?a ?b)
     (same-part-of-speech ?a ?b "V")
     (has-equal ?a ?b :declension)
     ;;(has-equal ?a ?b :number) FIX why not?
     ;;(has-equal ?a ?b :gender)
     (has-equal ?a ?b :variant)]
    [(consistent ?a ?b)
     (same-part-of-speech ?a ?b "N")
     (has-equal ?a ?b :declension)
     (has-equal ?a ?b :variant)]])

(defn match
  [{:keys [stem ending]}]
  (let [query {:find '[?e ?s] :in '[$ % ?stem ?ending]
               :where '[[?e :ending ?ending]
                        [?s :stem ?stem]
                        (consistent ?e ?s)]}]
    (-<>> (lp/q query rules stem ending)
          (map (partial map lp/eid->entity))
          (map (partial apply merge))
          clojure.pprint/print-table)))

;; TEST
(defn ♥ [] (match {:stem "can" :ending "is"}))
