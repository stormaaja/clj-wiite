(ns clj-wiite.file-store
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clj-wiite.store :refer [Store]]))

(defn- file-exists? [path]
  (.exists (io/as-file path)))

(defrecord FileStore [file]
  Store
  (write-state! [store state]
    (spit (:file store) (str state)))
  (load-state [store]
    (when (file-exists? (:file store))
      (let [v (slurp (:file store))]
        (edn/read-string v)))))

(defn file-store [file]
  (FileStore. file))
