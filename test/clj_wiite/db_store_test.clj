(ns clj-wiite.db-store-test
  (:require [clj-wiite.postgresql :refer [extend-protocols]]
            [clojure.test :refer :all]
            [clj-wiite.db-store :refer :all]
            [clj-wiite.store :refer :all]
            [clj-wiite.config :refer [config]]
            [clojure.java.jdbc :as jdbc]))

(extend-protocols)

(defn drop-table [conn]
  (jdbc/db-do-commands
    conn (jdbc/drop-table-ddl :wiite_states)))

(defmacro with-store [store & body]
  `(let [~store (db-store (:db config))]
     (do ~@body)
     (drop-table (:db config))))

(def example-state {:some "value" :other 2})

(deftest db-store-write-state
  (testing "Writing DB store state"
    (with-store store
      (write-state! store example-state)
      (is (= (load-state store) example-state)))))

(deftest db-store-load-state
  (testing "Loading DB store state"
    (with-store store
      (is (nil? (load-state store)))
      (write-state! store example-state)
      (is (= (load-state store) example-state))
      (let [changed-state (assoc example-state :some "String II" :num 3)]
        (write-state! store changed-state)
        (is (= (load-state store) changed-state))))))

(deftest db-store-recreate-store
  (testing "Recreating DB store"
    (with-store store
      (write-state! store example-state)
      (let [second-store (db-store (:db config))]
        (is (= (load-state second-store) example-state))))))
