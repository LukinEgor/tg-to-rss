(ns rss.db.config
  (:require [environ.core :refer [env]]))

(def db-spec {:dbtype "postgresql"
              :dbname (env :db-name)
              :host (env :db-host)
              :user (env :db-user)
              :password (env :db-password)})
