(ns ladino.parse
  (:require [schema.core   :as s]
            [schema.macros :as sm]
            [swiss-arrows.core :refer :all]
            [midje.sweet :refer :all]
            [clojure.string :as str]
            [ladino.schemata :as ls]
            [ladino.queries :as lq]
            [ladino.peer :as lp]
            [ladino.utils :refer :all]
            [clojure.pprint :refer :all]
            ))



;; 1. examine the ending of a word,

(sm/defn word-endings :- [{:stem s/String :ending s/String}]
  [word :- s/String]
  (let [lc (str/lower-case word)]
    (for [i (-> word count range reverse)]
      {:stem   (.substring lc 0 i)
       :ending (.substring lc i)})))


(facts* "on word-endings"
        (word-endings "Veritas")
        => (contains [{:stem "verit" :ending "as"}])
        (type (word-endings "verbum"))
        => clojure.lang.LazySeq)

;; 2. compare it with the standard endings,
;; 3. derive the possible stems that could be consistent,
;; 4. compare those stems with a dictionary of stems,
;; 5. eliminate those for which the ending is inconsistent with the dictionary stem (e.g., a verb ending with a noun dictionary item)
;; 6. if unsuccessful, it tries with a large set of prefixes and suffixes, and various tackons (e.g., -que),
;; 7. finally it tries various "dirty tricks" (e.g., "ae" may be replaced by "e", inp by imp, syncope, etc.),
;; 8. and it reports any resulting matches as possible interpretations.



;;;;;;;;;;;;;;;

(defn parse [word]
  (doseq [ending (word-endings word)]
    (print-table (lq/match ending))
    (println "")))
