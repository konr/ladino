(ns ladino.queries
  (:require [midje.sweet    :refer :all]
            [swiss-arrows.core :refer :all]
            [ladino.peer :as lp]
            [schema.core :as s]
            [schema.macros :as sm]
            [datomic.api :as d]))


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
     (has-equal ?a ?b :variant)]

    [(consistent ?a ?b)
     (same-part-of-speech ?a ?b "N")
     (has-equal ?a ?b :declension)
     (has-equal ?a ?b :variant)]

    [(consistent ?a ?b)
     (same-part-of-speech ?a ?b "INTERJ")
     (has-equal ?a ?b :declension)
     (has-equal ?a ?b :variant)]

    [(consistent ?a ?b)
     (same-part-of-speech ?a ?b "ADV")
     (has-equal ?a ?b :degree)]

    [(consistent ?a ?b)
     (same-part-of-speech ?a ?b "PREP")]

    [(consistent ?a ?b)
     (same-part-of-speech ?a ?b "CONJ")]

    [(consistent ?a ?b)
     (same-part-of-speech ?a ?b "ADJ")
     (has-equal ?a ?b :declension)
     (has-equal ?a ?b :variant)]

    [(consistent ?a ?b)
     (same-part-of-speech ?a ?b "PRON")
     (has-equal ?a ?b :declension)
     (has-equal ?a ?b :kind)
     (has-equal ?a ?b :variant)]

    [(consistent ?a ?b)
     (same-part-of-speech ?a ?b "NUM")
     (has-equal ?a ?b :declension)
     (has-equal ?a ?b :kind)
     (has-equal ?a ?b :amount)
     (has-equal ?a ?b :variant)]])

(defn match
  [{:keys [stem ending]}]
  (let [query {:find '[?e ?s] :in '[$ % ?stem ?ending]
               :where '[[?e :ending ?ending]
                        [?s :stem ?stem]
                        (consistent ?e ?s)]}]
    (-<>> (lp/q query rules stem ending)
          (map (partial map lp/eid->entity))
          (map (partial apply merge)))))
