(ns clj-wiite.watom
  (:require [clj-wiite.store :refer :all]
            [clojure.spec.alpha :as s])
  (:import [clj-wiite.store.Store]))

(def watom-key :watom)

(s/def ::store store?)

(s/fdef create-watom
        :args (s/cat :store ::store)
        :ret #(instance? clojure.lang.Atom %))

(defn create-watom [store]
  ^{:doc "Create Clojure Atom with watcher for writing state to store.
          Initial state will be loaded from store as well."
    :added "0.1.0"}
  (let [a (atom (load-state store))]
    (add-watch
      a watom-key
      (fn [k a old-state new-state]
        (write-state! store new-state)))
    a))
