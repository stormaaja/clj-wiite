(ns clj-wiite.postgresql
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.data.json :as json])
  (:import [org.postgresql.util PGobject]))

(defn value-to-json-pgobject [value]
  (doto (PGobject.)
    (.setType "json")
    (.setValue (json/write-str value))))

(defn extend-protocols []
  (extend-protocol jdbc/IResultSetReadColumn
    PGobject
    (result-set-read-column [pgobj metadata idx]
      (let [type (.getType pgobj)
            value (.getValue pgobj)]
        (if (= type "json")
          (json/read-str value :key-fn keyword)
          value))))

  (extend-protocol jdbc/ISQLValue
    clojure.lang.IPersistentMap
    (sql-value [value] (value-to-json-pgobject value))

    clojure.lang.IPersistentVector
    (sql-value [value] (value-to-json-pgobject value)))
  )
