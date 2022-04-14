(ns rss.api.server
  (:require [rss.api.channels :as channels]
            [rss.api.rss.channels :as rss-channels]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [compojure.core :refer :all]
            [compojure.coercions :refer [as-int]]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes main-routes
  (GET "/channels" [] (channels/index))
  (POST "/channels" [:as { { channel :channel } :body }] (channels/create channel))
  (DELETE "/channels/:id" [id :<< as-int] (channels/delete id))
  (GET "/rss/channels/:id" [id :<< as-int] (rss-channels/show id))
  (route/resources "/")
  (route/not-found "<h1>Page not found</h1>"))

(def app
  (->
   (handler/site main-routes)
   (wrap-json-body { :keywords? true })
   (wrap-json-response)))
