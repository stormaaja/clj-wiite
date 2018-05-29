# clj-wiite

Clojure library for using database backed Atom

## Usage

Set database connection as JDBC
[db-spec](http://clojure-doc.org/articles/ecosystem/java_jdbc/home.html). Give
db-spec to watom as a parameter and use it like regular Clojure
[atom](https://clojure.org/reference/atoms).

For example:

```
user> (require '[clj-wiite.core :as w])
nil
user> (def config {:dbtype "postgresql"
                   :dbname "wiite"
                   :host "localhost"
                   :user "wiiteuser"
                   :password "wiitepassword"})
#'user/config
user> (def watom (w/watom config))
#'user/watom
user> (reset! watom {:hello "World"})
{:hello "World"}
user> (swap! watom update :hello str "!")
{:hello "World!"}
```

## Environment variables

Define your environment before running tests. In other words tell tests name of
a config file. For example `test` will look for `config/test.edn` file.

## Testing

```  bash
ENVIRONMENT=test lein test
```

## License

Copyright © 2018 Matti Ahinko

Distributed under the MIT license.
