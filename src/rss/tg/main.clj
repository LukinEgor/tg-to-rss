(ns rss.tg.main
  (:require
   [rss.db.config :refer [db-spec]]
   [rss.tg.fetcher :refer [fetch-channel-info fetch-new-posts]]
   [honey.sql :as sql]
   [clojure.set :as set]
   [rss.db.mappers.channel :as channels-mapper]
   [rss.db.mappers.posts :as posts-mapper]))

(sync-posts db-spec)
(fetch-new-channels db-spec)

(defn sync-posts [db-spec]
  (let [channels
        (channels-mapper/get-channels db-spec [:!= :last_post_id nil])]
    (let [new-posts (fetch-posts channels)]
      (if (empty? new-posts)
        :empty
        ((posts-mapper/create db-spec new-posts)
         (channels-mapper/bulk-update-channels db-spec (extract-last-post-ids new-posts)))))))

(defn fetch-posts [channels]
  (->
   (map
    (fn [{ name :name last-post-id :last_post_id channel-id :id }]
      (let [posts (fetch-new-posts name last-post-id)]
        (if (empty? posts)
          []
          (map
           (fn [post]
             (-> post
                 (set/rename-keys { :id :tg-id })
                 (merge { :channel-id channel-id })))
           posts)))
      ) channels)
   (flatten)))


(defn take-last-post-id [posts]
  (-> posts
      (sort)
      (first)
      (:tg-id)))

(defn extract-last-post-ids [posts]
  (->> posts
      (group-by :channel-id)
      (map
       (fn
         [[id posts]]
         {:id id :last-post-id (take-last-post-id posts) }))))

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

