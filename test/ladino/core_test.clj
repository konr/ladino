(ns ladino.core-test
  (:require [midje.sweet :refer :all]
            [ladino.core :refer :all]
            [ladino.peer :as p]
            ;; Data sources
            [ladino.sources.stemlist :as lss]
            [ladino.sources.endings  :as lse]
            [ladino.parse :as parse]))



(fact "on how stuff from the .lat files ends up in the database"
      (future-fact "we pass the lines to the sources files")
      (import-from-sources!) => irrelevant
      (provided
       (lse/read-endings-file)  => [anything anything]
       (lss/read-stemlist-file) => [anything]
       (p/transact-one anything) => irrelevant))

(fact "on how we process command-line arguments"
      (future-fact "on multiple arguments")
      (-main ..veritas..) => ..truth..
      (provided
       (initialize) => irrelevant
       (parse/parse ..veritas..) => ..truth..))

(fact "on the steps of `initialize!`"
      (initialize!) => irrelevant
      (provided
       (init-db!) => anything
       (import-from-sources!) => anything))

(future-fact "on init-db!"
   (future-fact "on how it's in components, not here")
   (future-fact "on it's various parts, that should be broken down")
   (future-fact "on how we don't def the database, but rather set it on an atom"))
