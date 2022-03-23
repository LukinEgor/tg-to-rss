(ns rss.db.migrations
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl])
  (:require [rss.db.config :as config]))

(def migration-config
  {:datastore  (jdbc/sql-database config/db-spec)
   :migrations (jdbc/load-resources "migrations")})

(defn migrate []
  (repl/migrate (migration-config)))

(defn rollback []
  (repl/rollback (migration-config)))

;; (repl/migrate migration-config)
;; (repl/rollback migration-config)
