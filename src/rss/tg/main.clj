(ns rss.tg.main
  (:require
   [rss.db.config :refer [db-spec]]
   [rss.tg.fetcher :refer [fetch-channel-info]]
   [honey.sql :as sql]
   [rss.db.mappers.channel :as mapper]))

;; (defn fetch-new-posts [db-spec]
;;   (->
;;    (get-channels { last-post-id: !nil })
;;    (fetch-new-posts)
;;    ()
;;    ))

(defn update-last-post-ids [db-spec]
  (->
   (mapper/get-channels db-spec [:= :last_post_id nil])
   ;; (map :id, :name)
   ;; (map (fn [channel-id]))
   ;; (map { status: :new }) ; [{ :channel-id, :last-post-id }]
   ;; (update-channels)
   ))

(def data (mapper/get-channels db-spec [:= :last_post_id nil]))
data
(def test1 (map (fn [{ id :id name :name }]
                 (let [{ post-id :id content :content } (find-last-post name)]
                   { :id id :post-id post-id :content content })) data))

(def test2 (map (fn [{ id :id name :name }]
                 (let [info (fetch-channel-info name)]
                   (merge { :id id } info))) data))

test2

(mapper/bulk-update test2)

(map (fn [item] (+ item 1)) [1 2])
;; (update-last-post-ids db-spec)

