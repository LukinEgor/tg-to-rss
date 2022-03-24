(ns rss.db.mappers.channel
  (:require [clojure.java.jdbc :as jdbc])
  (:require [honey.sql :as sql]
            [honey.sql.helpers :refer :all :as h]
            [clojure.core :as c])
  (:require [rss.db.config :refer [db-spec]]))

;; update channels
;; set last_post_id=tmp.last_post_id
;; from (values (20, 1), (21, 5)) as tmp (id,last_post_id)
;; where channels.id=tmp.id;

(defn bulk-update [values]
  (jdbc/execute!
   db-spec
   (sql/format {:update :channels
                :set {:last_post_id :temp.last_post_id }
                :from {
                       :values [{ :id 20 :last_post_id 9}]
                       }
                :where [:= :id :channels.id]
                }))
  )

(bulk-update "a")

(defn bulk-update [values]
  (jdbc/execute! db-spec
                 (sql/format {:with [[[:temp]
                                      {:values [{ :id 20 :last_post_id 1 }]}]]
                              ;; {:values values }]]
                              :update :channels
                              :set {:channels.last_post_id :temp.last_post_id }
                              :where [:= :temp.id :channels.id]
                              })))

(bulk-update "test")

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

; update channels set last_post_id=tmp.last_post_id from (values (1,'new1'),(2,'new2'),(6,'new6')) as tmp (id,last_post_id) where channels.id=tmp.id;

(sql/format {:with [[[:temp {:columns [:id :description :last_post_id]}]
                     {:values [[1 "Sean" 0] [2 "Jay" 0]]}]]
             :update :channels
             ;; :set [:= :temp.description :channels.description]
             :set { :temp.* [:channels.*] }
             :where [:= :temp.id :channels.id]
             })

(defn update-channels-sql [channels]
  (->
   (h/with [[:temp {:columns :id}] {:values [:id 1] }])
   (h/update :channels)
   (h/set [])
   (h/from
    (h/values [[:id 1]])
    )
   (h/where )
   (sql/format)))

(update-channels-sql [])

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
