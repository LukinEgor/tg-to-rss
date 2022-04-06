(ns rss.db.migrations
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl])
  (:require [rss.db.config :as config]))

(defn load-config []
  {:datastore  (jdbc/sql-database config/db-spec)
   :migrations (jdbc/load-resources "migrations")})

(defn test []
  (println "hello world"))

(defn migrate []
  (repl/migrate (load-config)))

(defn rollback []
  (repl/rollback (load-config)))
