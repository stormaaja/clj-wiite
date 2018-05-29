(ns clj-wiite.core
  (:require [clj-wiite.watom :refer [create-watom]]
            [clj-wiite.db-store :refer [db-store]]))

(defn watom [conn]
  ^{:added "0.1.0"
    :doc "Create Clojure Atom with watch with two operations:
           - Changed Atom state is being written to DB store
           - Initial state will be loaded from DB store
          DB store will be initialized with parameters given."}
  (create-watom (db-store conn)))
