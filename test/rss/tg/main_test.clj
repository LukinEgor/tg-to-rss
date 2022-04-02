(ns rss.tg.main-test
  (:require [rss.tg.main :refer [download-new-channels sync-posts]]
            [rss.db.mappers.channel :as db]
            [rss.db.mappers.posts :as pdb]
            [rss.db.config :refer [db-spec]]
            [clojure.test :refer :all]))

(defn clear-db [f]
  (pdb/delete-all db-spec)
  (db/delete-all db-spec)
  (f))

(use-fixtures :each clear-db)

(def test-cover-post "cover")
(def test-post "content")
(def test-channel { :name "addmeto" })

(defn mock-fetch-post [last-post-id]
  (fn [_ post-id]
    (if (and
         (<= post-id last-post-id)
         (not (= post-id 0))
         ) test-post test-cover-post)))

(deftest test-download-new-channels
  (db/create-channel db-spec test-channel)

  (let [last-post-id (+ (rand-int 1000) 1)
        fetch-post (mock-fetch-post last-post-id)]
    (download-new-channels db-spec fetch-post)

    (let [channels (db/get-channels db-spec)]
      (is (= (count channels) 1))
      (let [channel (first channels)]
        (is (= (:description channel) test-cover-post))

        (let [last-post (first (pdb/get db-spec [:= :channel_id (:id channel)]))]
          (is (= (:content last-post) test-post))
          (is (= (:tg_id last-post) last-post-id)))))))

(deftest test-sync-posts
  (let [last-post-id (+ (rand-int 1000) 1)
        fetch-post (mock-fetch-post last-post-id)]

    (db/create-channel db-spec (merge
                                { :last-post-id last-post-id :description test-cover-post }
                                test-channel))

    (sync-posts db-spec (mock-fetch-post (+ last-post-id 1)))

    (let [posts (pdb/get db-spec)]
      (is (= (count posts) 1))
      (let [last-post (first posts)]
        (is (= (:content last-post) test-post))
        (is (= (:tg_id last-post) (+ last-post-id 1)))))))
