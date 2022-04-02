(ns rss.db.mappers.posts
  (:require [clojure.java.jdbc :as jdbc])
  (:require [honey.sql :as sql]
            [honey.sql.helpers :refer :all :as h]
            [clojure.core :as c])
  (:require [rss.db.config :refer [db-spec]]))

(defn get-sql [pred]
  (-> (h/select [:*])
      (h/from :posts)
      (h/where pred)
      (sql/format)))

(defn get
  ([db-spec] (get db-spec []))
  ([db-spec pred] (jdbc/query db-spec (get-sql pred))))

(defn create-sql [posts]
  (-> (h/insert-into :posts)
      (h/values posts)
      (sql/format)))

(defn create [db-spec posts]
  (jdbc/execute! db-spec (create-sql posts) { :return-keys true }))

(defn delete-all-sql []
  (-> (h/delete-from :posts)
      (sql/format)))

(defn delete-all [db-spec]
  (jdbc/execute! db-spec (delete-all-sql)))
