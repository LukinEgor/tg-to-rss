(ns rss.db.mappers.channel
  (:require [clojure.java.jdbc :as jdbc])
  (:require [honey.sql :as sql]
            [honey.sql.helpers :refer :all :as h]
            [clojure.core :as c])
  (:require [rss.db.config :refer [db-spec]]))


;; (require '[clojure.java.jdbc :as jdbc])

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
  (jdbc/execute! db-spec (create-channel-sql channel)))

;; (def channel {:name "test" :description "desc" :link "link" :status "new" })

;; (get-channels-sql)
;; (get-channels config/db-spec)
;; (create-channel-sql channel)
;; (create-channel config/db-spec channel)
