(ns clj-wiite.watom
  (:require [clj-wiite.store :refer :all])
  (:import [clj-wiite.store.Store]))

(def watom-key :watom)

(defn- create-watcher [store]
  (fn [k a old-state new-state]
    (write-state! store new-state)))

(defn create-watom [store x & {:keys [load-state? store-key]}]
  (let [watch-key (or store-key watom-key)
        load? (if (some? load-state?) load-state? true)
        state (when load? (load-state store))
        a (atom (or state x))
        watcher (create-watcher store)]
    (add-watch a watch-key watcher)
    (watcher watch-key a x x)
    a))
