(ns rss.db.mappers.posts
  (:require [clojure.java.jdbc :as jdbc])
  (:require [honey.sql :as sql]
            [honey.sql.helpers :refer :all :as h]
            [clojure.core :as c])
  (:require [rss.db.config :refer [db-spec]]))

(defn create-sql [posts]
  (-> (h/insert-into :posts)
      (h/values posts)
      (sql/format)))

(defn create [db-spec posts]
  (jdbc/execute! db-spec (create-sql posts) { :return-keys true }))
