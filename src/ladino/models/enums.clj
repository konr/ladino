(ns ladino.models.enums)

(def age-dictionary
  [{:db/ident  :whitaker.age/unknown
    :db/doc "In use throughout the ages/unknown -- the default"
    :whitaker/representation "X"}
   {:db/ident  :whitaker.age/archaic
    :db/doc "Very early forms, obsolete by classical times"
    :whitaker/representation "A"}
   {:db/ident  :whitaker.age/early
    :db/doc "Early Latin, pre-classical, used for effect/poetry"
    :whitaker/representation "B"}
   {:db/ident  :whitaker.age/classical
    :db/doc "Limited to classical (~150 BC - 200 AD)"
    :whitaker/representation "C"}
   {:db/ident  :whitaker.age/late
    :db/doc "Late, post-classical (3rd-5th centuries)"
    :whitaker/representation "D"}
   {:db/ident  :whitaker.age/later
    :db/doc "Latin not in use in Classical times (6-10) Christian"
    :whitaker/representation "E"}
   {:db/ident  :whitaker.age/medieval
    :db/doc "Medieval (11th-15th centuries)"
    :whitaker/representation "F"}
   {:db/ident  :whitaker.age/scholar
    :db/doc "Latin post 15th - Scholarly/Scientific   (16-18)"
    :whitaker/representation "G"}
   {:db/ident  :whitaker.age/modern
    :db/doc "Coined recently, words for new things (19-20)"
    :whitaker/representation "H"}])

(def frequency
  [{:db/ident  :whitaker.frequency/unknown
    :whitaker/representation "X"
    :db/doc "Unknown or unspecified"}
   {:db/ident  :whitaker.frequency/very-frequent
    :whitaker/representation "A"
    :db/doc "Very frequent, in all Elementry Latin books, top 1000+ words"}
   {:db/ident  :whitaker.frequency/frequent
    :whitaker/representation "B"
    :db/doc "Frequent, next 2000+ words"}
   {:db/ident  :whitaker.frequency/common
    :whitaker/representation "C"
    :db/doc "For Dictionary, in top 10,000 words"}
   {:db/ident  :whitaker.frequency/lesser-common
    :whitaker/representation "D"
    :db/doc "For Dictionary, in top 20,000 words"}
   {:db/ident  :whitaker.frequency/uncommon
    :whitaker/representation "E"
    :db/doc "2 or 3 citations"}
   {:db/ident  :whitaker.frequency/very-rare
    :whitaker/representation "F"
    :db/doc "Having only single citation in OLD or L+S"}
   {:db/ident  :whitaker.frequency/inscription
    :whitaker/representation "I"
    :db/doc "Only citation is inscription"}
   {:db/ident  :whitaker.frequency/graffiti
    :whitaker/representation "M"
    :db/doc "Presently not much used"}
   {:db/ident  :whitaker.frequency/pliny
    :whitaker/representation "N"
    :db/doc "Things that appear only in Pliny Natural History"}])

(def areas
  [{:db/ident  :whitaker.area/unknown
    :whitaker/representation "X"
    :db/doc "All or none"}
   {:db/ident  :whitaker.area/agriculture
    :whitaker/representation "A"
    :db/doc "Agriculture, Flora, Fauna, Land, Equipment, Rural"}
   {:db/ident  :whitaker.area/biological
    :whitaker/representation "B"
    :db/doc "Biological, Medical, Body Parts"}
   {:db/ident  :whitaker.area/drama
    :whitaker/representation "D"
    :db/doc "Drama, Music, Theater, Art, Painting, Sculpture"}
   {:db/ident  :whitaker.area/ecclesiastic
    :whitaker/representation "E"
    :db/doc "Ecclesiastic, Biblical, Religious"}
   {:db/ident  :whitaker.area/grammar
    :whitaker/representation "G"
    :db/doc "Grammar, Retoric, Logic, Literature, Schools"}
   {:db/ident  :whitaker.area/legal
    :whitaker/representation "L"
    :db/doc "Legal, Government, Tax, Financial, Political, Titles"}
   {:db/ident  :whitaker.area/poetic
    :whitaker/representation "P"
    :db/doc "Poetic"}
   {:db/ident  :whitaker.area/science
    :whitaker/representation "S"
    :db/doc "Science, Philosophy, Mathematics, Units/Measures"}
   {:db/ident  :whitaker.area/technical
    :whitaker/representation "T"
    :db/doc "Technical, Architecture, Topography, Surveying"}
   {:db/ident  :whitaker.area/war
    :whitaker/representation "W"
    :db/doc "War, Military, Naval, Ships, Armor"}
   {:db/ident  :whitaker.area/mythology
    :whitaker/representation "Y"
    :db/doc "Mythology"}])

(def source
  [{:db/ident  :whitaker.source/unknown
    :whitaker/representation "X"
    :db/doc "General or unknown or too common to say"}
   {:db/ident  :whitaker.source/no-info-1
    :whitaker/representation "A"
    :db/doc ""}
   {:db/ident  :whitaker.source/bee
    :whitaker/representation "B"
    :db/doc "C.H.Beeson, A Primer of Medieval Latin, 1925 (Bee)"}
   {:db/ident  :whitaker.source/cas
    :whitaker/representation "C"
    :db/doc "Charles Beard, Cassell's Latin Dictionary 1892 (Cas)"}
   {:db/ident  :whitaker.source/sex
    :whitaker/representation "D"
    :db/doc "J.N.Adams, Latin Sexual Vocabulary, 1982 (Sex)"}
   {:db/ident  :whitaker.source/ecc
    :whitaker/representation "E"
    :db/doc "L.F.Stelten, Dictionary of Eccles. Latin, 1995 (Ecc)"}
   {:db/ident  :whitaker.source/def
    :whitaker/representation "F"
    :db/doc "Roy J. Deferrari, Dictionary of St. Thomas Aquinas, 1960 (DeF)"}
   {:db/ident  :whitaker.source/g+l
    :whitaker/representation "G"
    :db/doc "Gildersleeve + Lodge, Latin Grammar 1895 (G+L)"}
   {:db/ident  :whitaker.source/yves
    :whitaker/representation "H"
    :db/doc "Collatinus Dictionary by Yves Ouvrard "}
   {:db/ident  :whitaker.source/lexicon
    :whitaker/representation "I"
    :db/doc "Leverett, F.P., Lexicon of the Latin Language, Boston 1845"}
   {:db/ident  :whitaker.source/bracton
    :whitaker/representation "J"
    :db/doc "Bracton: De Legibus Et Consuetudinibus Angliæ"}
   {:db/ident  :whitaker.source/cal
    :whitaker/representation "K"
    :db/doc "Calepinus Novus, modern Latin, by Guy Licoppe (Cal)"}
   {:db/ident  :whitaker.source/lewis
    :whitaker/representation "L"
    :db/doc "Lewis, C.S., Elementary Latin Dictionary 1891"}
   {:db/ident  :whitaker.source/latham
    :whitaker/representation "M"
    :db/doc "Latham, Revised Medieval Word List, 1980 (Latham)"}
   {:db/ident  :whitaker.source/nel
    :whitaker/representation "N"
    :db/doc "Lynn Nelson, Wordlist (Nel)"}
   {:db/ident  :whitaker.source/old
    :whitaker/representation "O"
    :db/doc "Oxford Latin Dictionary, 1982 (OLD)"}
   {:db/ident  :whitaker.source/souter
    :whitaker/representation "P"
    :db/doc "Souter, A Glossary of Later Latin to 600 A.D., Oxford 1949 (Souter)"}
   {:db/ident  :whitaker.source/other
    :whitaker/representation "Q"
    :db/doc "Other, cited or unspecified dictionaries"}
   {:db/ident  :whitaker.source/plater
    :whitaker/representation "R"
    :db/doc "Plater + White, A Grammar of the Vulgate, Oxford 1926 (Plater)"}
   {:db/ident  :whitaker.source/l+s
    :whitaker/representation "S"
    :db/doc "Lewis and Short, A Latin Dictionary, 1879 (L+S)"}
   {:db/ident  :whitaker.source/translation
    :whitaker/representation "T"
    :db/doc "Found in a translation  --  no dictionary reference"}
   {:db/ident  :whitaker.source/no-info-2
    :whitaker/representation "U"
    :db/doc ""}
   {:db/ident  :whitaker.source/saxo
    :whitaker/representation "V"
    :db/doc "Vademecum in opus Saxonis - Franz Blatt (Saxo)"}
   {:db/ident  :whitaker.source/whitaker
    :whitaker/representation "W"
    :db/doc "My personal guess, mostly obvious extrapolation (Whitaker or W)   "}
   {:db/ident  :whitaker.source/temp
    :whitaker/representation "Y"
    :db/doc "Temp special code"}
   {:db/ident  :whitaker.source/user
    :whitaker/representation "Z"
    :db/doc "Sent by user --  no dictionary reference"}])

(def geo
  [{:db/ident  :whitaker.geo/unknown
    :whitaker/representation "X"
    :db/doc "All or none"}
   {:db/ident  :whitaker.geo/africa
    :whitaker/representation "A"
    :db/doc "Africa"}
   {:db/ident  :whitaker.geo/britania
    :whitaker/representation "B"
    :db/doc "Britian"}
   {:db/ident  :whitaker.geo/china
    :whitaker/representation "C"
    :db/doc "China"}
   {:db/ident  :whitaker.geo/scandinavia
    :whitaker/representation "D"
    :db/doc "Scandinavia"}
   {:db/ident  :whitaker.geo/egypt
    :whitaker/representation "E"
    :db/doc "Egypt"}
   {:db/ident  :whitaker.geo/gaul
    :whitaker/representation "F"
    :db/doc "France, Gaul"}
   {:db/ident  :whitaker.geo/germania
    :whitaker/representation "G"
    :db/doc "Germany"}
   {:db/ident  :whitaker.geo/hellas
    :whitaker/representation "H"
    :db/doc "Greece"}
   {:db/ident  :whitaker.geo/italia
    :whitaker/representation "I"
    :db/doc "Italy, Rome"}
   {:db/ident  :whitaker.geo/india
    :whitaker/representation "J"
    :db/doc "India"}
   {:db/ident  :whitaker.geo/balkans
    :whitaker/representation "K"
    :db/doc "Balkans"}
   {:db/ident  :whitaker.geo/netherlands
    :whitaker/representation "N"
    :db/doc "Netherlands"}
   {:db/ident  :whitaker.geo/persia
    :whitaker/representation "P"
    :db/doc "Persia"}
   {:db/ident  :whitaker.geo/near-east
    :whitaker/representation "Q"
    :db/doc "Near East"}
   {:db/ident  :whitaker.geo/russia
    :whitaker/representation "R"
    :db/doc "Russia"}
   {:db/ident  :whitaker.geo/iberia
    :whitaker/representation "S"
    :db/doc "Spain, Iberia"}
   {:db/ident  :whitaker.geo/eastern-europa
    :whitaker/representation "U"
    :db/doc "Eastern Europe"}])


(def all-enums [age-dictionary frequency areas source geo])
