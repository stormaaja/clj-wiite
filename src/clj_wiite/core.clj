(ns clj-wiite.core
  (:require [clj-wiite.watom :refer [create-watom]]))

(defn watom
  ^{:added "0.1.0"
    :doc "Create Clojure Atom with watch with two operations:
           - Changed Atom state is being written to store
           - Initial state will be loaded from store"}
  [store] (create-watom store))
