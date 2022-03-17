(ns rss.api.channels
  (:require [rss.db.config :refer [db-spec]])
  (:require [rss.db.mappers.channel :as mapper]))

(defn index [request]
  (mapper/get-channels db-spec))

;; (defn create [request]
;;   (->> request
;;        (validate-channel)
;;        (create-channel db-spec)))
