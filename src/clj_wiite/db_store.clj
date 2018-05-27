(ns clj-wiite.db-store
  (:use clj-wiite.postgresql)
  (:require [clojure.spec.alpha :as s]
            [clojure.edn :as edn]
            [clj-wiite.store :refer :all]
            [clojure.java.jdbc :as jdbc])
  (:import [org.postgresql.util PSQLException]))

(def ^:private table-not-exists-state "42P01")

(s/def ::conn (s/keys :req [::dbtype ::dbname ::host]
                      :opt [::user ::password ::ssl ::sslfactory]))

(defn- table-exists? [conn]
  (some?
    (try
      (jdbc/query
        conn
        ["SELECT state FROM wiite_states ORDER BY id DESC LIMIT 0"])
      (catch PSQLException e
        (when-not (= (.getSQLState e) table-not-exists-state)
          (throw e))))))

(defn- create-table! [conn]
  (jdbc/db-do-commands
    conn
    (jdbc/create-table-ddl
      :wiite_states
      [[:id "serial"]
       [:created_at "timestamp with time zone" "default now()"]
       [:state "json"]])))

(defn- select-latest-state [conn]
  (first
    (jdbc/query
     conn
     ["SELECT state FROM wiite_states ORDER BY id DESC LIMIT 1"]
     {:row-fn #(get-in % [:state :value])})))

(defn- insert-latest-state! [conn state]
  (jdbc/insert!
    conn
    :wiite_states
    {:state {:value state}}))


(defrecord DBStore [conn]
  ^{:doc "Store for keeping state in database.
          Currently only PostgreSQL-database is fully supported"
    :added "0.1.0"}
  Store
  (write-state! [store state]
    (insert-latest-state! (:conn store) state))
  (load-state [store]
    (select-latest-state (:conn store))))

(s/fdef db-store
        :args (s/cat :params ::params)
        :ret #(instance? DBStore %))

(defn db-store [conn]
  ^{:doc "Store for keeping state in database.
          Tables needed are being created if they don't exist.
          Currently only PostgreSQL-database is fully supported
          See: https://github.com/clojure/java.jdbc"
    :added "0.1.0"
    :pre [(s/valid? ::conn conn)]}
  (when-not (table-exists?) (create-table! conn))
  (DBStore. conn))
