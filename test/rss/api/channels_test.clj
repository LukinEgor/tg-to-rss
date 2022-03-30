(ns rss.api.channels_test
  (:require [rss.db.mappers.channel :as db])
  (:require [rss.db.config :refer [db-spec]])
  (:require [rss.api.server :refer [app]]
            [ring.mock.request :as mock]
            [clojure.test :refer :all]))

;; (defn clear-db [f]
;;   (db/delete-all db-spec)
;;   (f))

;; (use-fixtures :each clear-db)

;; (def test-channel {:name "test" :description "desc" :link "link" })

;; (deftest test-get-channels
;;   (let [resp (app (mock/request :get "/channels"))]
;;     (is (= (:status resp) 200))))

;; (deftest test-create-channel
;;   (is (= (count (db/get-channels db-spec)) 0))

;;   (let [resp (app (->
;;                   (mock/request :post "/channels")
;;                   (mock/json-body { :channel test-channel })))]

;;     (is (= (:status resp) 200))
;;     (is (= (count (db/get-channels db-spec)) 1))))

;; (deftest test-delete-channel
;;   (let [channel (db/create-channel db-spec test-channel)]
;;     (let [resp (app (mock/request
;;                      :delete
;;                      (str "/channels/" (:id channel))))]

;;       (is (= (:status resp) 200))
;;       (is (= (count (db/get-channels db-spec)) 0)))))
