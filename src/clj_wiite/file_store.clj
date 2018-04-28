(ns clj-wiite.file-store
  (:require [clojure.edn :as edn]
            [clj-wiite.store :refer [Store]]))

(defrecord FileStore [file]
  Store
  (write-state [store state]
    (spit (:file store) (str state)))
  (load-state [store]
    (let [v (slurp (:file store))]
      (edn/read-string v))))

(defn file-store [file]
  (FileStore. file))
