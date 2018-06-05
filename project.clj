(defproject clj-wiite "0.1.0"
  :description "Clojure library for using database backed Atom"
  :url "https://github.com/stormaaja/clj-wiite"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/java.jdbc "0.7.6"]
                 [org.postgresql/postgresql "42.2.2"]
                 [org.clojure/data.json "0.2.6"]]
  :aliases {"checkall" ["do" ["check"] ["kibit"] ["eastwood"] ["bikeshed"]]}
  :profiles {:dev {:dependencies [[org.clojure/test.check "0.9.0"]]}})
