(ns clj-wiite.store)

(defprotocol Store
  (write-state! [store state])
  (load-state [store ]))
