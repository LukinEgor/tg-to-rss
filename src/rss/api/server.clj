(ns rss.api.server
  (:require [rss.api.channels :as channels-api])
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes main-routes
  (GET "/" [] "<h1>Hello World compojure</h1>")
  (GET "/channels" [] channels-api/index)
  (route/not-found "<h1>Page not found</h1>"))

(def app (handler/site main-routes))

;; (defn app [request]
;;   {:status 200
;;    :headers {"Content-Type" "text/plain"}
;;    :body "Hello world."})
