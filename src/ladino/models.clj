(ns ladino.models)

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



(def endings-model
  {:stem             {}
   :part-of-speech   {}
   :declension       {}
   :key              {}
   :reference        {}
   :degree           {}
   :case             {}
   :variant          {}
   :gender           {}
   :number           {}
   :transitivity     {}
   :kind             {}
   :amount           {}})

(def all-attributes (conj endings-model inflects-model))
