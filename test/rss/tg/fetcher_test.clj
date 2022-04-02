(ns rss.tg.fetcher-test
  (:require [rss.tg.fetcher :refer [fetch-new-posts fetch-post find-last-post meta-content-property]]
            [clojure.test :refer :all]))

(def channel-name "addmeto")
(def test-cover-post "cover")
(def test-post "content")

(defn mock-fetch-post [last-post-id]
  (fn [_ post-id]
    (if (and
         (<= post-id last-post-id)
         (not (= post-id 0))
         ) test-post test-cover-post)))

(deftest test-fetch-post
  (let [mock-fetch (fn [_] (slurp "/usr/src/app/test/rss/fixtures/tg-post.html"))
        content (fetch-post mock-fetch channel-name 0)]
    (is (= (and
            (= (type content) java.lang.String)
            (not (empty? content)))))))

(deftest test-find-last-post
  (let [last-post-id (+ (rand-int 1000) 1)
        fetch-post (mock-fetch-post last-post-id)]
    (let [{ id :last-post-id } (find-last-post fetch-post channel-name)]
      (is (= id last-post-id)))))

(deftest test-fetch-new-posts
  (let [last-post-id (+ (rand-int 1000) 1)
        new-posts-count (rand-int 10)
        fetch-post (mock-fetch-post (+ last-post-id new-posts-count))]
    (let [posts (fetch-new-posts fetch-post channel-name test-cover-post last-post-id)]
      (is (= (count posts) new-posts-count)))))
