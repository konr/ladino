(ns ladino.parse
  (:require [schema.core   :as s]
            [schema.macros :as sm]
            [clojure.string :as str]
            [midje.sweet    :refer :all]))


;;;;;;;;;;;;;
;;; Utils ;;;
;;;;;;;;;;;;;

(defmacro facts* [title & forms]
  `(facts ~title
          (s/with-fn-validation
            ~@forms)))

;;;;;;;;;;;;;;;;;
;;; Algorithm ;;;
;;;;;;;;;;;;;;;;;

;; 1. examine the ending of a word,
(sm/defn word-endings :- [s/String]
  [word :- s/String]
  (for [i (-> word count range reverse)]
    (.substring word i)))


(facts* "on word-endings"
        (word-endings "Veritas")
        => ["s" "as" "tas" "itas" "ritas" "eritas" "Veritas"]
        (type (word-endings "verbum"))
        => clojure.lang.LazySeq)

;; 2. compare it with the standard endings,

(def endings-file "inflects.lat")

(sm/defn process-line :- (s/maybe [s/String])
  [parsed-line :- (s/maybe [s/String])]
  ;; (frequencies (read-endings-file))
  ;; => {0 1422, 6 9, 5 2, 11 419, 10 21, 12 341, 13 647, 14 346}
  (let [masks {5  [:part-of-speech :case :declension] ;; TODO
               6  []   ;; TODO
               10 []   ;; TODO
               11 []   ;; TODO
               12 []   ;; TODO
               13 []   ;; TODO
               14 []}] ;; TODO
    (when (not (nil? parsed-line))
      (zipmap (get masks (count parsed-line)) parsed-line))))

(sm/defn parse-line :- [s/String]
  [line :- s/String]
  (let [remove-comments (fn [prefix x] (str/replace x (re-pattern (str prefix ".*")) "")) ;; TODO extract
        remove-ada-comments (partial remove-comments "--")]
    (->> line remove-ada-comments (re-seq #"\w+"))))

(sm/defn read-endings-file []
  (let [file->lines     (comp str/split-lines slurp clojure.java.io/resource)]
    (loop [[h & tail :as lines] (file->lines endings-file)
           edn []]
      (if (empty? lines) edn
          (if-let [parsed-line (-> h parse-line process-line)]
            (recur tail (conj edn parsed-line))
            (recur tail edn))))))

;; 3. derive the possible stems that could be consistent,
;; 4. compare those stems with a dictionary of stems,
;; 5. eliminate those for which the ending is inconsistent with the dictionary stem (e.g., a verb ending with a noun dictionary item),
;; 6. if unsuccessful, it tries with a large set of prefixes and suffixes, and various tackons (e.g., -que),
;; 7. finally it tries various "dirty tricks" (e.g., "ae" may be replaced by "e", inp by imp, syncope, etc.),
;; 8. and it reports any resulting matches as possible interpretations.
