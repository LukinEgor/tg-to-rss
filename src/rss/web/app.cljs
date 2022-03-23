(ns rss.web.app
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
   [cljs.core.async :refer [<!]]
   [cljs-http.client :as http]
   [reagent.core :as r]
   [reagent.dom :as rdom]))

(def tg-channels-url "/channels")

(defonce tg-channels (r/atom []))

; cljs.core/PersistentVector
(defn fetch-channels []
  (go (let [response (<! (http/get tg-channels-url))]
        (println (type (:body response)))
        (swap! tg-channels (fn [items] (:body response)))
        )))
        ;; (swap! tg-channels (:body response)))))

;; (go (let [response (<! (http/get "data.edn"))]
;;   (prn (:status response))
;;   (prn (:body response))))


(defn channels-list [tg-channels]
  [:div
   [:div "tg-channels" tg-channels]
   [:ul
    (for [channel tg-channels]
      ^{:id channel} [channel-component channel])]
   [:input {:type "button" :value "Click me!"
            :on-click #(fetch-channels)}]])

(defn channel-component[channel]
  [:div
   [:div "Channel:" (:name channel)]
   [:input {:type "button" :value "Click me!"
            :on-click #(fetch-channels)}]])

(defn render []
  (rdom/render
   [channels-list tg-channels]
   (js/document.getElementById "root")))

(render)
