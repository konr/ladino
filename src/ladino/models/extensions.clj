(ns ladino.models.extensions)


(def schema-attributes
  [{:db/ident :whitaker/representation
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "The representation in Whitaker's Words files"
    :db.install/_attribute :db.part/db}])
