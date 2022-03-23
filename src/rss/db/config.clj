(ns rss.db.config)
  ;; (require [environ.core :refer [env]]))

(def db-spec {:dbtype "postgresql"
            :dbname "rss_test"
            :host "db"
            :user "postgres"
            :password "" })
