(ns rss.api.channels
  (:require [rss.db.config :refer [db-spec]]
            [ring.util.response :refer [response]]
            [rss.db.mappers.channel :as mapper]))

(defn index []
  (->
   (mapper/get-channels db-spec)
   (response)))

(defn create [channel]
  (->> channel
       ;; (validate-channel)
       (mapper/create-channel db-spec)
       (response)))

(defn delete [id]
  (->> id
       (mapper/delete-channel db-spec)
       (response)))
