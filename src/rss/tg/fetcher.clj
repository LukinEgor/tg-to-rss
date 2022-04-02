(ns rss.tg.fetcher
  (:use pl.danieljanus.tagsoup)
  (:require [clojure.string :as str]))

(def tg-url "https://t.me")
(def cover-post-id 0)
(def started-post-id 100000)
(def meta-content-property "twitter:description")

(defn- take-headers
  [html-vec]
  (first
   (filterv (fn [item]
              (and
               (= (type item) clojure.lang.PersistentVector) ;TODO find more idiomatic solution
               (= (first item) :head))
              ) html-vec)))

(defn- twitter-meta-header? [item]
  (if (= (type item) clojure.lang.PersistentVector)
    (let [[key { name :name content :content }] item]
      (and (= key :meta) (= name meta-content-property)))
    false))

(defn- extract-content [[_ { content :content }]] content)

(defn fetch-post [fetch channel post-id]
  (->>
   (str tg-url "/" channel "/" post-id)
   (fetch)
   (parse-string)
   (take-headers)
   (filter twitter-meta-header?)
   (first)
   (extract-content)))

(defn last-post? [current first-next second-next intro]
  (and
   (not (= current first-next))
   (= first-next intro)
   (= second-next intro)))

(defn next-guess-id [guess-id step intro current-post]
  (if (= current-post intro)
    (- guess-id step)
    (+ guess-id step)))

; refactoring
(defn- find-last-post-iter [fetch-post channel intro step guess-id]
  (let [current-post (fetch-post channel guess-id)
        first-next (fetch-post channel (+ guess-id 1))
        second-next (fetch-post channel (+ guess-id 2))]
    (if (or
         (last-post? current-post first-next second-next intro)
         (and (= step 1) (not (= current-post intro))))
      { :last-post-id guess-id :content current-post :cover-post intro }
      (find-last-post-iter fetch-post
                           channel
                           intro
                           (int (Math/ceil (/ step 2)))
                           (next-guess-id guess-id step intro current-post)))))

(defn find-last-post
  "Iteratively find last post."
  [fetch-post channel]
  (let [cover-post (fetch-post channel cover-post-id)]
    (find-last-post-iter fetch-post
                         channel
                         cover-post
                         (int (/ started-post-id 2))
                         started-post-id)))

(defn without-new-posts? [current-post first-next second-next cover]
  (and
   (= current-post cover)
   (= first-next cover)
   (= second-next cover)))

;; TODO refactoring
(defn- fetch-new-posts-iter
  [fetch-post channel cover-post post-id last-post-id posts]
  (let [current-post (fetch-post channel post-id)
        first-next (fetch-post channel (+ post-id 1))
        second-next (fetch-post channel (+ post-id 2))]
    (cond
      (without-new-posts? current-post first-next second-next cover-post) []
      (last-post? current-post first-next second-next cover-post) (conj posts { :id post-id :content current-post })
      (= (+ last-post-id 20) post-id) (conj posts { :id post-id :content current-post })
      :else (fetch-new-posts-iter fetch-post
                                  channel
                                  cover-post
                                  (+ post-id 1)
                                  last-post-id
                                  (conj posts { :id post-id :content current-post }))
      )))

(defn fetch-new-posts
  [fetch-post channel cover-post last-post-id]
  (fetch-new-posts-iter fetch-post
                        channel
                        cover-post
                        (+ last-post-id 1)
                        last-post-id
                        []))
