(ns rss.tg.sync
  (:use pl.danieljanus.tagsoup))

(use 'pl.danieljanus.tagsoup)
;; (fetch-post-by-id "addmeto" 4789)
;; (fetch-post-by-id "addmeto" 0)

(def tg-url "https://t.me")

(def intro-post-id 0)
(def started-post-id 100000)

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
      (and (= key :meta) (= name "twitter:description")))
    false))

;(89)
; 100 ;50
; 50 ;25
; 75 ;13
; 88 ;7
; 95 ;4
; 91 ;2
; 89 ;1

; intro = fetch(0)
; step = started-post-id
; start_post = fetch(started-post-id)
; (= start_post intro)
; last-post? current-port next-post intro-content
; find-last-post-id-rec []



(defn find-last-post [channel]
  (let [intro (fetch-post-by-id channel intro-post-id)]
    (find-last-post-iter channel
                            intro
                            (/ started-post-id 2)
                            started-post-id)))

(defn find-last-post-iter [channel intro step guess-id]
  (let [current-post (fetch-post-by-id channel guess-id)
        next-post (fetch-post-by-id channel (+ guess-id 1))]
    (println guess-id)
    (if (last-post? current-post next-post intro)
      guess-id
      (find-last-post-iter channel
                              intro
                              (Math/ceil (/ step 2))
                              (next-guess-id guess-id step intro current-post)))))

(defn next-guess-id [guess-id step intro current-post]
  (if (= current-post intro)
    (- guess-id step)
    (+ guess-id step)))

(defn last-post? [current next intro]
  (and (not (= current next)) (= next intro)))


(find-last-post "addmeto")
(find-last-post "oldlentach")
