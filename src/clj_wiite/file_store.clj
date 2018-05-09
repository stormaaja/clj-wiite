(ns clj-wiite.file-store
  (:require [clojure.spec.alpha :as s]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clj-wiite.store :refer :all]))

(s/fdef file-exists?
        :args (s/cat :path string?)
        :ret boolean?)

(defn- file-exists? [path]
  (.exists (io/as-file path)))

(defrecord FileStore [file]
  ^{:doc "Store for keeping state in a given file.
          File doesn't have to exist. If it does, it will be overwritten.
          If file does not exist, load-state will return nil."
    :added "0.1.0"}
  Store
  (write-state! [store state]
    (spit (:file store) (str state)))
  (load-state [store]
    (when (file-exists? (:file store))
      (let [v (slurp (:file store))]
        (edn/read-string v)))))

(s/fdef file-store
        :args (s/cat :file string?)
        :ret #(instance? FileStore %))

(defn file-store [file]
  ^{:doc "Store for keeping state in a given file.
          File doesn't have to exist. If it does, it will be overwritten."
    :added "0.1.0"}
  (FileStore. file))
