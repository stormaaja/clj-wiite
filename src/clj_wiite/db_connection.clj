(ns clj-wiite.db-connection
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.data.json :as json])
  (:import [org.postgresql.util PGobject]))

(def pg-db {:dbtype "postgresql"
            :dbname "wiite"
            :host "localhost"
            :user "wiiteuser"
            :password "passu"})

(def table :wiite_states)

(extend-protocol jdbc/ISQLValue
  clojure.lang.IPersistentMap
  (sql-value [value]
    (doto (PGobject.)
      (.setType "json")
      (.setValue (json/write-str value)))))

(extend-protocol jdbc/IResultSetReadColumn
  PGobject
  (result-set-read-column [pgobj metadata idx]
    (let [type  (.getType pgobj)
          value (.getValue pgobj)]
      (case type
        "json" (json/read-str value :key-fn keyword)
        :else value))))

(defn value-to-json-pgobject [value]
  (doto (PGobject.)
    (.setType "json")
      (.setValue (json/write-str value))))

(extend-protocol jdbc/ISQLValue
  clojure.lang.IPersistentMap
  (sql-value [value] (value-to-json-pgobject value))

  clojure.lang.IPersistentVector
  (sql-value [value] (value-to-json-pgobject value)))

(defn create-table []
  (jdbc/db-do-commands
    pg-db
    (jdbc/create-table-ddl
      table
      [[:id "serial"]
       [:created_at "timestamp with time zone" "default now()"]
       [:state "json"]])))

(defn select-latest-state []
  (first
    (jdbc/query
     pg-db
     [(format "SELECT state FROM %s ORDER BY id DESC LIMIT 1" (name table))]
     {:row-fn :state})))

(defn insert-latest-state [state]
  (jdbc/insert!
    pg-db
    table
    {:state state}))
