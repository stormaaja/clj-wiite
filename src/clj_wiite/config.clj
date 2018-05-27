(ns clj-wiite.config
  (:require [clojure.edn :as edn]))

(defonce config
  (edn/read-string (slurp (format "config/%s.edn" (System/getenv "ENVIRONMENT")))))
