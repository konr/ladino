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

#_(sm/defn process-line :- (s/maybe [s/String])
    [parsed-line :- (s/maybe [s/String])]
    ;; (frequencies (read-endings-file))
    ;; => {0 1422, 6 9, 5 2, 11 419, 10 21, 12 341, 13 647, 14 346}
    (let [masks {5  [:part-of-speech                              :declension :dunno :age :frequency]
                 6  [:part-of-speech        :case                 :declension :dunno :age :frequency]
                 10 [:part-of-speech :♥ :♥♥ :case :number :gender :declension :dunno :age :frequency]
                 11 [:part-of-speech :♥ :♥♥ :case :number :gender :declension :dunno :age :frequency]
                 12 []
                 13 []
                 14 []}]
      (when (not (nil? parsed-line))
        (zipmap (get masks (count parsed-line)) parsed-line))))

(defmethod process-line :default [elements]
  "HUEAHUEHUAHEUHAUEA"
  )

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
  elements)

(defmethod process-line "PRON" [elements]
  elements)

(defmethod process-line "SUPINE" [elements]
  elements)

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
          (if-let [parsed-line (-> h parse-line #_process-line)]
            (recur tail (conj edn parsed-line))
            (recur tail edn))))))

;; 3. derive the possible stems that could be consistent,
;; 4. compare those stems with a dictionary of stems,
;; 5. eliminate those for which the ending is inconsistent with the dictionary stem (e.g., a verb ending with a noun dictionary item),
;; 6. if unsuccessful, it tries with a large set of prefixes and suffixes, and various tackons (e.g., -que),
;; 7. finally it tries various "dirty tricks" (e.g., "ae" may be replaced by "e", inp by imp, syncope, etc.),
;; 8. and it reports any resulting matches as possible interpretations.
