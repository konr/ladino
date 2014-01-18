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

(def age-dictionary
  {:whitaker/unknown-age   {:db/doc "In use throughout the ages/unknown -- the default"
                            :whitaker/representation "X"}
   :whitaker/archaic-age   {:db/doc "Very early forms, obsolete by classical times"
                            :whitaker/representation "A"}
   :whitaker/early-age     {:db/doc "Early Latin, pre-classical, used for effect/poetry"
                            :whitaker/representation "B"}
   :whitaker/classical-age {:db/doc "Limited to classical (~150 BC - 200 AD)"
                            :whitaker/representation "C"}
   :whitaker/late-age      {:db/doc "Late, post-classical (3rd-5th centuries)"
                            :whitaker/representation "D"}
   :whitaker/later-age     {:db/doc "Latin not in use in Classical times (6-10) Christian"
                            :whitaker/representation "E"}
   :whitaker/medieval-age  {:db/doc "Medieval (11th-15th centuries)"
                            :whitaker/representation "F"}
   :whitaker/scholar-age   {:db/doc "Latin post 15th - Scholarly/Scientific   (16-18)"
                            :whitaker/representation "G"}
   :whitaker/modern-age    {:db/doc "Coined recently, words for new things (19-20)"
                            :whitaker/representation "H"}})

(def frequency
  {:whitaker/unknown-freq  {:whitaker/representation "X"
                            :db/doc "Unknown or unspecified"}
   :whitaker/very-frequent {:whitaker/representation "A"
                            :db/doc "Very frequent, in all Elementry Latin books, top 1000+ words"}
   :whitaker/frequent      {:whitaker/representation "B"
                            :db/doc "Frequent, next 2000+ words"}
   :whitaker/common        {:whitaker/representation "C"
                            :db/doc "For Dictionary, in top 10,000 words"}
   :whitaker/lesser-common {:whitaker/representation "D"
                            :db/doc "For Dictionary, in top 20,000 words"}
   :whitaker/uncommon      {:whitaker/representation "E"
                            :db/doc "2 or 3 citations"}
   :whitaker/very-rare     {:whitaker/representation "F"
                            :db/doc "Having only single citation in OLD or L+S"}
   :whitaker/inscription   {:whitaker/representation "I"
                            :db/doc "Only citation is inscription"}
   :whitaker/graffiti      {:whitaker/representation "M"
                            :db/doc "Presently not much used"}
   :whitaker/pliny         {:whitaker/representation "N"
                            :db/doc "Things that appear only in Pliny Natural History"}})

(def areas
  {:whitaker/unknown      {:whitaker/representation "X"
                           :db/doc "All or none"}
   :whitaker/agriculture  {:whitaker/representation "A"
                           :db/doc "Agriculture, Flora, Fauna, Land, Equipment, Rural"}
   :whitaker/biological   {:whitaker/representation "B"
                           :db/doc "Biological, Medical, Body Parts"}
   :whitaker/drama        {:whitaker/representation "D"
                           :db/doc "Drama, Music, Theater, Art, Painting, Sculpture"}
   :whitaker/ecclesiastic {:whitaker/representation "E"
                           :db/doc "Ecclesiastic, Biblical, Religious"}
   :whitaker/grammar      {:whitaker/representation "G"
                           :db/doc "Grammar, Retoric, Logic, Literature, Schools"}
   :whitaker/legal        {:whitaker/representation "L"
                           :db/doc "Legal, Government, Tax, Financial, Political, Titles"}
   :whitaker/poetic       {:whitaker/representation "P"
                           :db/doc "Poetic"}
   :whitaker/science      {:whitaker/representation "S"
                           :db/doc "Science, Philosophy, Mathematics, Units/Measures"}
   :whitaker/technical    {:whitaker/representation "T"
                           :db/doc "Technical, Architecture, Topography, Surveying"}
   :whitaker/war          {:whitaker/representation "W"
                           :db/doc "War, Military, Naval, Ships, Armor"}
   :whitaker/mythology    {:whitaker/representation "Y"
                           :db/doc "Mythology"}})

(def source
  {:whitaker/unknown     {:whitaker/representation "X"
                          :db/doc "General or unknown or too common to say"}
   :whitaker/no-info-1   {:whitaker/representation "A"
                          :db/doc ""}
   :whitaker/bee         {:whitaker/representation "B"
                          :db/doc "C.H.Beeson, A Primer of Medieval Latin, 1925 (Bee)"}
   :whitaker/cas         {:whitaker/representation "C"
                          :db/doc "Charles Beard, Cassell's Latin Dictionary 1892 (Cas)"}
   :whitaker/sex         {:whitaker/representation "D"
                          :db/doc "J.N.Adams, Latin Sexual Vocabulary, 1982 (Sex)"}
   :whitaker/ecc         {:whitaker/representation "E"
                          :db/doc "L.F.Stelten, Dictionary of Eccles. Latin, 1995 (Ecc)"}
   :whitaker/def         {:whitaker/representation "F"
                          :db/doc "Roy J. Deferrari, Dictionary of St. Thomas Aquinas, 1960 (DeF)"}
   :whitaker/g+l         {:whitaker/representation "G"
                          :db/doc "Gildersleeve + Lodge, Latin Grammar 1895 (G+L)"}
   :whitaker/yves        {:whitaker/representation "H"
                          :db/doc "Collatinus Dictionary by Yves Ouvrard "}
   :whitaker/lexicon     {:whitaker/representation "I"
                          :db/doc "Leverett, F.P., Lexicon of the Latin Language, Boston 1845"}
   :whitaker/bracton     {:whitaker/representation "J"
                          :db/doc "Bracton: De Legibus Et Consuetudinibus Angliæ"}
   :whitaker/cal         {:whitaker/representation "K"
                          :db/doc "Calepinus Novus, modern Latin, by Guy Licoppe (Cal)"}
   :whitaker/lewis       {:whitaker/representation "L"
                          :db/doc "Lewis, C.S., Elementary Latin Dictionary 1891"}
   :whitaker/latham      {:whitaker/representation "M"
                          :db/doc "Latham, Revised Medieval Word List, 1980 (Latham)"}
   :whitaker/nel         {:whitaker/representation "N"
                          :db/doc "Lynn Nelson, Wordlist (Nel)"}
   :whitaker/old         {:whitaker/representation "O"
                          :db/doc "Oxford Latin Dictionary, 1982 (OLD)"}
   :whitaker/souter      {:whitaker/representation "P"
                          :db/doc "Souter, A Glossary of Later Latin to 600 A.D., Oxford 1949 (Souter)"}
   :whitaker/other       {:whitaker/representation "Q"
                          :db/doc "Other, cited or unspecified dictionaries"}
   :whitaker/plater      {:whitaker/representation "R"
                          :db/doc "Plater + White, A Grammar of the Vulgate, Oxford 1926 (Plater)"}
   :whitaker/l+s         {:whitaker/representation "S"
                          :db/doc "Lewis and Short, A Latin Dictionary, 1879 (L+S)"}
   :whitaker/translation {:whitaker/representation "T"
                          :db/doc "Found in a translation  --  no dictionary reference"}
   :whitaker/no-info-2   {:whitaker/representation "U"
                          :db/doc ""}
   :whitaker/saxo        {:whitaker/representation "V"
                          :db/doc "Vademecum in opus Saxonis - Franz Blatt (Saxo)"}
   :whitaker/whitaker    {:whitaker/representation "W"
                          :db/doc "My personal guess, mostly obvious extrapolation (Whitaker or W)   "}
   :whitaker/temp        {:whitaker/representation "Y"
                          :db/doc "Temp special code"}
   :whitaker/user        {:whitaker/representation "Z"
                          :db/doc "Sent by user --  no dictionary reference"}})

(def geo
  {:whitaker/unknown        {:whitaker/representation "X" :db/doc "All or none"}
   :whitaker/africa         {:whitaker/representation "A" :db/doc "Africa"}
   :whitaker/britania       {:whitaker/representation "B" :db/doc "Britian"}
   :whitaker/china          {:whitaker/representation "C" :db/doc "China"}
   :whitaker/scandinavia    {:whitaker/representation "D" :db/doc "Scandinavia"}
   :whitaker/egypt          {:whitaker/representation "E" :db/doc "Egypt"}
   :whitaker/gaul           {:whitaker/representation "F" :db/doc "France, Gaul"}
   :whitaker/germania       {:whitaker/representation "G" :db/doc "Germany"}
   :whitaker/hellas         {:whitaker/representation "H" :db/doc "Greece"}
   :whitaker/italia         {:whitaker/representation "I" :db/doc "Italy, Rome"}
   :whitaker/india          {:whitaker/representation "J" :db/doc "India"}
   :whitaker/balkans        {:whitaker/representation "K" :db/doc "Balkans"}
   :whitaker/netherlands    {:whitaker/representation "N" :db/doc "Netherlands"}
   :whitaker/persia         {:whitaker/representation "P" :db/doc "Persia"}
   :whitaker/near-east      {:whitaker/representation "Q" :db/doc "Near East"}
   :whitaker/russia         {:whitaker/representation "R" :db/doc "Russia"}
   :whitaker/iberia         {:whitaker/representation "S" :db/doc "Spain, Iberia"}
   :whitaker/eastern-europa {:whitaker/representation "U" :db/doc "Eastern Europe"}})
