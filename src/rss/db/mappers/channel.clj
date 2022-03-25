(ns rss.db.mappers.channel
  (:require [clojure.java.jdbc :as jdbc])
  (:require [honey.sql :as sql]
            [honey.sql.helpers :refer :all :as h]
            [clojure.core :as c])
  (:require [rss.db.config :refer [db-spec]]))

(defn get-channels-sql [pred]
  (-> (h/select [:*])
      (h/from :channels)
      (h/where pred)
      (sql/format)))

(defn get-channels
  ([db-spec] (get-channels db-spec []))
  ([db-spec pred] (jdbc/query db-spec (get-channels-sql pred))))

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

(defn bulk-update-channels [db-spec channels]
  (let [values (map
                (fn [{ id :id description :description last-post-id :last-post-id }]
                  [description last-post-id id]) channels)]
    (jdbc/db-do-prepared
     db-spec
     (concat
      ["UPDATE channels SET description = ?, last_post_id = ? WHERE id = ?"]
      values)
     { :multi? true })))
