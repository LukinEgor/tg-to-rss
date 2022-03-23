(ns rss.api.channels
  (:require [rss.db.config :refer [db-spec]])
  (:require [ring.util.response :refer [response]])
  (:require [rss.db.mappers.channel :as mapper]))

(defn index [channel]
  (mapper/get-channels db-spec))

;; (defn create [channel]
;;   (mapper/create-channel db-spec channel))

(defn create [channel]
  (->> channel
       ;; (validate-channel)
       (mapper/create-channel db-spec)
       (response)))

(defn delete [id]
  (->> id
       (mapper/delete-channel db-spec)
       (response)))
