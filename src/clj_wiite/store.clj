(ns clj-wiite.store)

(defprotocol Store
  (write-state! [store state] "Write state to store")
  (load-state [store] "Load state from store"))

(defn store? [x]
  (satisfies? Store x))
