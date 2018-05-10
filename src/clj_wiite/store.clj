(ns clj-wiite.store)

(defprotocol Store
  (write-state! [store state] "Write state to store")
  (load-state [store] "Load state from store"))

(defn store? [x]
  ^{:doc "Function for checking if given instance implements Store protocol"
    :added "0.1.0"}
  (satisfies? Store x))
