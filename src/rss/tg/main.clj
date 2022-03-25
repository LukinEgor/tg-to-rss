(ns rss.tg.main
  (:require
   [rss.db.config :refer [db-spec]]
   [rss.tg.fetcher :refer [fetch-channel-info]]
   [honey.sql :as sql]
   [rss.db.mappers.channel :as channels-mapper]
   [rss.db.mappers.posts :as posts-mapper]
   ))

;; (defn fetch-new-posts [db-spec]
;;   (->
;;    (get-channels { last-post-id: !nil })
;;    (fetch-new-posts)
;;    ()
;;    ))

;; https://t.me/c/1155329936/1833
;; https://t.me/OpenLongevity_ru/4062
(defn fetch-new-channels [db-spec]
  (let [new-channels (channels-mapper/get-channels db-spec [:= :last_post_id nil])]
    (let [downloaded-channels (map (fn [{ id :id name :name }]
                                     (let [info (fetch-channel-info name)]
                                       (merge { :id id } info))) new-channels)]
      (channels-mapper/bulk-update-channels db-spec downloaded-channels)
      (let [last-posts (map
                        (fn [{ tg-id :last-post-id channel-id :id content :last-post }]
                          { :channel-id channel-id :tg-id tg-id :content content })
                        downloaded-channels
                        )]
        (posts-mapper/create db-spec last-posts)
        ))))

;; (fetch-new-channels db-spec)

   ;; (map :id, :name)
   ;; (map (fn [channel-id]))
   ;; (map { status: :new }) ; [{ :channel-id, :last-post-id }]
   ;; (update-channels)

;; (def data (channels-mapper/get-channels db-spec [:!= :last_post_id nil]))
;; data
;; (def test1 (map (fn [{ id :id name :name }]
;;                  (let [{ post-id :id content :content } (find-last-post name)]
;;                    { :id id :post-id post-id :content content })) data))

;; (def test2 (map (fn [{ id :id name :name }]
;;                  (let [info (fetch-channel-info name)]
;;                    (merge { :id id } info))) data))

;; test2

;; (def last-posts (map
;;                  (fn [{ tg-id :last-post-id channel-id :id content :last-post }]
;;                    { :channel-id channel-id :tg-id tg-id :content content })
;;                  test2))

;; last-posts
;; (posts-mapper/create db-spec last-posts)

;; ;; ;; (ch-mapper/bulk-update-channels db-spec test2)
;; ;; (channels-mapper/bulk-update-channels db-spec test2)

;; ;; (posts-mapper/create [{ :content "content" :channel_id 20 :tg_id 999 } { :id 1 :content "content" :channel_id 20 :tg_id 999 }])

;; ;; (map (fn [item] (+ item 1)) [1 2])
;; ;; (update-last-post-ids db-spec)
