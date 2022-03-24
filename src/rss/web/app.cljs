(ns rss.web.app
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
   [cljs.core.async :refer [<!]]
   [cljs-http.client :as http]
   [reagent.core :as r]
   [reagent.dom :as rdom]))

(defn tg-channels-url [] "/channels")
(defn tg-channel-url [id] (str "/channels/" id))

(defonce tg-channels (r/atom []))

(defn fetch-channels []
  (go (let [response (<! (http/get (tg-channels-url)))]
        (println (type (:body response)))
        (swap! tg-channels (fn [items] (:body response)))
        )))

(defn create-channel [channel]
  (go (let [response (<! (http/post
                          (tg-channels-url)
                          { :json-params { :channel channel } }
                          ))]
        (println (type (:body response)))
        )))

(defn delete-channel [id]
  (go (let [response (<! (http/delete (tg-channel-url id)))]
        (println (type (:body response)))
        )))

(defn handle-create-channel-button-click []
  (let [name (.-value (js/document.getElementById "name"))
        desc (.-value (js/document.getElementById "desc"))
        link (.-value (js/document.getElementById "link"))]
    (create-channel { :name name :description desc :link link })))

(defn create-channel-component []
  [:div
   [:div "Add channel"]
   [:input { :id "name" }]
   [:input { :id "desc" }]
   [:input { :id "link" }]
   [:input {:type "button" :value "Create"
            :on-click #(handle-create-channel-button-click)}]])

(defn channels-list [tg-channels]
  [:div
   [create-channel-component]
   (if (pos? (count @tg-channels))
     [:ul
      (for [channel @tg-channels]
        ^{:key (:id channel)} [channel-component channel])]
     [:div "empty"])
   [:input {:type "button" :value "Reload!"
            :on-click #(fetch-channels)}]])

(defn channel-component [channel]
  [:div
   [:div "Channel:" (:name channel) (:description channel)]
   [:input {:type "button" :value "delete"
            :on-click #(delete-channel (:id channel))}]])


(defn render []
  (rdom/render
   [channels-list tg-channels]
   (js/document.getElementById "root")))

(render)
