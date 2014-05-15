(defproject ladino "0.1.0-SNAPSHOT"
  :description "a modern latin dictionary"
  :url "http://github.com/konr/ladino"
  :license {:name "GPL"
            :url "http://www.gnu.org/licenses/"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [prismatic/schema "0.1.8"]
                 [com.datomic/datomic-free "0.9.4384"]
                 [com.stuartsierra/component "0.2.1"]
                 [swiss-arrows "0.6.0"]]
  :plugins [[lein-midje "3.1.3"]]
  :profiles {:dev {:source-paths ["config"]
                   :dependencies [[midje "1.6.0" :exclusions [org.clojure/clojure]]]}}
  :main ladino.core)
