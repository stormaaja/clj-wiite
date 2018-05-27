(ns clj-wiite.config
  (:require [clojure.edn :as edn]))

(defonce config
  (edn/read (slurp (format "config/%s.clj" (System/getenv "ENV")))))
