(ns clj-wiite.file-store-test
  (:require [clojure.test :refer :all]
            [clj-wiite.file-store :refer :all]
            [clj-wiite.store :refer :all]
            [clojure.java.io :as io]))

(def example-data {:some "String" :num 2 })

(defn random-file []
  (io/file (System/getProperty "java.io.tmpdir")
           (str (java.util.UUID/randomUUID))))

(deftest file-store-write-state
  (testing "Writing store state"
    (let [file (random-file)
          store (file-store (.getAbsolutePath file))]
      (is (not (.exists file)))
      (write-state! store example-data)
      (is (.exists file))
      (io/delete-file file))))

(deftest file-store-load-state
  (testing "Loading store state"
    (let [file (random-file)
          store (file-store (.getAbsolutePath file))]
      (io/delete-file file true)
      (is (nil? (load-state store)))
      (write-state! store example-data)
      (is (= (load-state store) example-data))
      (let [changed-state (assoc example-data :some "String II" :num 3)]
        (write-state! store changed-state)
        (is (= (load-state store) changed-state)))
      (io/delete-file file))))
