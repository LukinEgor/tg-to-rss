(ns rss.db.mappers.channel
  (:require [clojure.java.jdbc :as jdbc])
  (:require [honey.sql :as sql]
            [honey.sql.helpers :refer :all :as h]
            [clojure.core :as c])
  (:require [rss.db.config :refer [db-spec]]))

(defn get-channels-sql []
  (-> (h/select [:*])
      (h/from :channels)
      (sql/format)))

(defn get-channels [db-spec]
  (jdbc/query db-spec (get-channels-sql)))

(defn create-channel-sql [channel]
  (-> (h/insert-into :channels)
      (h/values [channel])
      (sql/format)))

(defn create-channel [db-spec channel]
  (jdbc/execute! db-spec (create-channel-sql channel) { :return-keys true }))

(defn delete-channel-sql [id]
  (-> (h/delete-from :channels)
      (h/where [:= :id id ])
      (sql/format)))

(defn delete-channel [db-spec id]
  (jdbc/execute! db-spec (delete-channel-sql id)))

(defn delete-all-sql []
  (-> (h/delete-from :channels)
      (sql/format)))

(defn delete-all [db-spec]
  (jdbc/execute! db-spec (delete-all-sql)))
