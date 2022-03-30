(ns rss.tg.main-test
  (:require [rss.tg.main :refer [download-new-channels]]
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
(def test-channel {:name "addmeto" :description nil })

(defn mock-fetch-post [last-post-id]
  (fn [_ post-id]
    (if (and
         (<= post-id last-post-id)
         (not (= post-id 0))
         ) test-post test-cover-post)))

;; (def a (download-new-channels db-spec (mock-fetch-post 19)))
;; a

;; (db/create-channel db-spec test-channel)
;; (db/get-channels db-spec)
;; ;; (pdb/delete-all db-spec)
;; (pdb/get db-spec [:= :channel_id 33])
;; (db/delete-channel db-spec 29)

;; (first (db/get-channels db-spec))

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
          (is (= (:tg-id last-post) last-post-id))
          )))))
