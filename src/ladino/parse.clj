(ns ladino.parse
  (:require [schema.core   :as s]
            [schema.macros :as sm]
            [swiss-arrows.core :refer :all]
            [clojure.string :as str]
            [midje.sweet    :refer :all]))


;;;;;;;;;;;;;
;;; Utils ;;;
;;;;;;;;;;;;;

(defmacro facts* [title & forms]
  `(facts ~title
          (s/with-fn-validation
            ~@forms)))

(defn resource->lines [uri]
  ((comp str/split-lines slurp clojure.java.io/resource) uri))

;;;;;;;;;;;;;;;;;
;;; Algorithm ;;;
;;;;;;;;;;;;;;;;;

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

(def endings-file "inflects.lat")

(defmethod process-line "INTERJ" [elements]
  (zipmap [:part-of-speech :declension :dunno :age :frequency] elements))

(defmethod process-line "ADV" [elements]
  (zipmap [:part-of-speech :degree :declension :dunno :age :frequency] elements))

(defmethod process-line "PREP" [elements]
  (zipmap [:part-of-speech :case :declension :dunno :age :frequency] elements))

(defmethod process-line "CONJ" [elements]
  (zipmap [:part-of-speech :declension :dunno :age :frequency] elements))

(defmethod process-line "N" [elements]
  (zipmap (case (count elements)
            10 [:part-of-speech :declension :dunno2 :case :number :gender :dunno :dunno3         :age :frequency]
            11 [:part-of-speech :declension :dunno2 :case :number :gender :dunno :dunno3 :ending :age :frequency])
          elements))

(defmethod process-line "ADJ" [elements]
  (zipmap (case (count elements)
            11 [:part-of-speech :declension :dunno2 :case :number :gender :degree :dunno :dunno3         :age :frequency]
            12 [:part-of-speech :declension :dunno2 :case :number :gender :degree :dunno :dunno3 :ending :age :frequency])
          elements))

(defmethod process-line "V" [elements]
  (zipmap (case (count elements)
            12 [:part-of-speech :declension :dunno2 :tense :voice :mood :person :number :dunno3 :dunno4         :age :frequency]
            13 [:part-of-speech :declension :dunno2 :tense :voice :mood :person :number :dunno3 :dunno4 :ending :age :frequency])
          elements))

(defmethod process-line "VPAR" [elements]
  (zipmap [:part-of-speech :declension :dunno2 :case :number :gender
           :tense :voice :dunno5 :dunno3 :dunno4 :ending :age :frequency]
          elements))

(defmethod process-line "PRON" [elements]
  (zipmap (case (count elements)
            10 [:part-of-speech :declension :dunno2 :case :number :gender :dunno3 :dunno4         :age :frequency]
            11 [:part-of-speech :declension :dunno2 :case :number :gender :dunno3 :dunno4 :ending :age :frequency])
          elements))

(defmethod process-line "SUPINE" [elements]
  (zipmap [:part-of-speech :declension :dunno :case :number :gender :dunno3 :dunno4 :ending :age :frequency]
          elements))

(defmethod process-line "NUM" [elements]
  (zipmap (case (count elements)
            11 [:part-of-speech :declension :dunno :case :number :gender :dunno3 :dunno4 :ending :age :frequency]
            12 [:part-of-speech :declension :dunno :case :number :gender :some-sort-of-type-i-guess
                :dunno3 :dunno4 :ending :age :frequency])
          elements))

(defmulti process-line first)

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
          (if-let [parsed-line (-?<> h parse-line process-line)]
            (recur tail (conj edn parsed-line))
            (recur tail edn))))))

;; 3. derive the possible stems that could be consistent,
;; 4. compare those stems with a dictionary of stems,

(def stemlist-file "stemlist.lat")

;; FIX continue from here. always a part of speech has only one amount
;; of elements


(defmulti process-stemlist-line second)
(defmethod process-stemlist-line "INTERJ" [elements]   (count elements))
(defmethod process-stemlist-line "ADV" [elements]      (count elements))
(defmethod process-stemlist-line "PREP" [elements]     (count elements))
(defmethod process-stemlist-line "CONJ" [elements]     (count elements))
(defmethod process-stemlist-line "N" [elements]        (count elements))
(defmethod process-stemlist-line "ADJ" [elements]      (count elements))
(defmethod process-stemlist-line "V" [elements]        (count elements))
(defmethod process-stemlist-line "PRON" [elements]     (count elements))
(defmethod process-stemlist-line "NUM" [elements]      (count elements))
(defmethod process-stemlist-line "PACK" [elements]     (count elements))
(defmethod process-stemlist-line :default [elements]
  (process-stemlist-line (concat [""] elements)))


(defn read-stemlist-file []
    (->> stemlist-file resource->lines
         (map (partial re-seq #"\w+"))
         (map process-stemlist-line)
         distinct
         #_(zipmap [:stem :part-of-speech :declension :dunno2 ])))



;; 5. eliminate those for which the ending is inconsistent with the dictionary stem (e.g., a verb ending with a noun dictionary item),
;; 6. if unsuccessful, it tries with a large set of prefixes and suffixes, and various tackons (e.g., -que),
;; 7. finally it tries various "dirty tricks" (e.g., "ae" may be replaced by "e", inp by imp, syncope, etc.),
;; 8. and it reports any resulting matches as possible interpretations.



;;;;;;;;;;;;;;;

(defn parse [word]
  (let [endings (word-endings word)
        db (read-endings-file)]
    (map ;; FIX don't do this for all digits
     (fn [{:keys [stem ending] :as all}]
       (hash-map all (filter #(= (:ending %) ending) db)))
     endings)))
