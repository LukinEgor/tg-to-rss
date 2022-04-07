(ns rss.db.migrations
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [clojure.java.jdbc]
            [rss.db.config :as config]))

(defn load-config []
  {:datastore  (jdbc/sql-database config/db-spec)
   :migrations (jdbc/load-resources "migrations")})

(defn migrate []
  (repl/migrate (load-config)))

(defn rollback []
  (repl/rollback (load-config)))

(defn create-db []
  (->>
   (str
    "CREATE DATABASE "
    (:dbname config/db-spec))
    (clojure.java.jdbc/query config/db-spec)))
