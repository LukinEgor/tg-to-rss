(ns rss.tg.main
  (:require
   [rss.db.config :refer [db-spec]]
   [rss.tg.fetcher :refer [fetch-post find-last-post fetch-new-posts]]
   [honey.sql :as sql]
   [clojure.set :as set]
   [rss.db.mappers.channel :as channels-mapper]
   [rss.db.mappers.posts :as posts-mapper]))

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

(defn get-new-channels [db-spec]
  (channels-mapper/get-channels db-spec [:= :last_post_id nil]))

(defmacro unless [pred a b]
  `(if (not ~pred) ~a ~b))

(defn find-last-posts [fetch-post channels]
  (map
   (fn [{ id :id name :name }]
     (merge { :id id } (find-last-post fetch-post name)))
   channels))

(defn save-channels [db-spec channels]
  (->>
   (map
    (fn [channel]
      (->
       channel
       (clojure.set/rename-keys { :cover-post :description })
       (dissoc :content))
      ) channels)
   (channels-mapper/bulk-update-channels db-spec)))

(defn save-last-posts [db-spec channels]
  (->>
   (map
    (fn [{ tg-id :last-post-id channel-id :id content :content }]
      { :channel-id channel-id :tg-id tg-id :content content })
    channels)
   (posts-mapper/create db-spec)))

(defn db-save [db-spec channels]
  (save-channels db-spec channels)
  (save-last-posts db-spec channels))

(defn download-new-channels [db-spec fetch-post]
  (->>
   db-spec
   (get-new-channels)
   ((fn [channels]
     (unless
      (empty? channels)
      (->>
       (find-last-posts fetch-post channels)
       (db-save db-spec))
      [])))))

;; (find-last-posts (partial fetch-post slurp) channels)

;; (defn fetch-posts [channels]
;;   (->
;;    (map
;;     (fn [{ name :name last-post-id :last_post_id channel-id :id }]
;;       (let [posts (fetch-new-posts name last-post-id)]
;;         (if (empty? posts)
;;           []
;;           (map
;;            (fn [post]
;;              (-> post
;;                  (set/rename-keys { :id :tg-id })
;;                  (merge { :channel-id channel-id })))
;;            posts)))
;;       ) channels)
;;    (flatten)))

;; (defn sync-posts [db-spec]
;;   (let [channels
;;         (channels-mapper/get-channels db-spec [:!= :last_post_id nil])]
;;     (let [new-posts (fetch-posts channels)]
;;       (if (empty? new-posts)
;;         :empty
;;         ((posts-mapper/create db-spec new-posts)
;;          (channels-mapper/bulk-update-channels db-spec (extract-last-post-ids new-posts)))))))


;; MOVE to main layer
;; (defn fetch-channel-info [fetch channel]
;;   "Fetch telegram channel info - last-post-id
;; Arguments:
;; - function that return a html
;; - string"
;;   (let [{ id :id content :content } (find-last-post channel fetch)
;;         intro (fetch-post-by-id channel cover-post-id fetch)]
;;     { :last-post-id id :last-post content :description intro }))
