(ns clj-wiite.store)

(defprotocol Store
  (write-state! "Write state to store" [store state])
  (load-state "Load state from store" [store ]))
