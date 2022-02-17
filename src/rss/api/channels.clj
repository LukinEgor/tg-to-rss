(ns rss.api.channels)

(defn index [request]
  (->> request
       (get-channels)))

;; (defn create [request]
;;   (->> request
;;        (validate-channel)
;;        (create-channel)))
