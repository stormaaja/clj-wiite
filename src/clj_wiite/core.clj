(ns clj-wiite.core
  (:require [clj-wiite.watom :refer [create-watom]]))

(defn watom
  ([store x & options] (apply create-watom store x options))
  ([store x] (create-watom store x :load-state? true)))
