(ns rss.tg.sync
  (:use pl.danieljanus.tagsoup))

;; (find-last-post "addmeto")

(def tg-url "https://t.me")
(def intro-post-id 0)
(def started-post-id 100000)
(def meta-content-property "twitter:description")

(defn find-last-post [channel]
  (let [intro (fetch-post-by-id channel intro-post-id)]
    (find-last-post-iter channel
                         intro
                         (/ started-post-id 2)
                         started-post-id)))

(defn find-last-post-iter [channel intro step guess-id]
  (let [current-post (fetch-post-by-id channel guess-id)
        next-post (fetch-post-by-id channel (+ guess-id 1))
        next-post-2 (fetch-post-by-id channel (+ guess-id 2))]
    (println step "-" guess-id)
    (if (or
         (last-post? current-post next-post next-post-2 intro)
         (and (= step 1.0) (not (= current-post intro))))
      { :id guess-id :content current-post }
      (find-last-post-iter channel
                              intro
                              (Math/ceil (/ step 2))
                              (next-guess-id guess-id step intro current-post)))))

(defn fetch-post-by-id [channel post-id]
  (->>
   (str tg-url "/" channel "/" post-id)
   (parse)
   (take-headers)
   (filter twitter-meta-header?)
   (first)
   (extract-content)))

(defn take-headers [html] (nth html 2))

(defn extract-content [[_ { content :content }]] content)

(defn twitter-meta-header? [item]
  (if (= (type item) clojure.lang.PersistentVector)
    (let [[key { name :name content :content }] item]
      (and (= key :meta) (= name meta-content-property)))
    false))

(defn next-guess-id [guess-id step intro current-post]
  (if (= current-post intro)
    (- guess-id step)
    (+ guess-id step)))

(defn last-post? [current next next2 intro]
  (and (not (= current next)) (= next intro) (= next2 intro)))
