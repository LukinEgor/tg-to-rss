(ns rss.db.config
  (require [environ.core :refer [env]])
  ;; (require [clojure.java.jdbc :as jdbc])
  (require [ragtime.jdbc :as jdbc])
  )

;; (def database-url
;;   (env :database-url))

;; (require '[clojure.java.jdbc :as jdbc])

;; (def pg-db {:connection-uri (str "postgresql://db:5432/rss_dev")})
;; (def pg-db {:connection-uri (str "jdbc:postgresql://db:5432/rss_dev")})

(def pg-db {:dbtype "postgresql"
            :dbname "rss_dev"
            :host "db"
            :user "postgres"
            :password "" })

;; (env :database-url))

;; (jdbc/db-do-commands pg-db ["select * from pg_stat"])

(def migrations-config
  {:datastore  (jdbc/sql-database pg-db)
   :migrations (jdbc/load-resources "migrations")})


