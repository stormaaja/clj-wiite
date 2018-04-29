(ns clj-wiite.core
  (:require [clj-wiite.watom :refer [create-watom]]))

(defn watom
  ^{:added "0.1.0"
    :doc "Create Clojure Atom with watch with two operations:
           - Changed Atom state is being written to store
           - Initial state will be loaded from store
          Optional parameter (options) can be
           - :load-state? Wheter load state from store or use given value x
           - :store-key Key to give for store and to use with atom"}
  ([store x & options] (apply create-watom store x options))
  ([store x] (create-watom store x :load-state? true)))
