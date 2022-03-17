(ns rss.api.server-test
  (:require [rss.api.server :refer [app]]
            [ring.mock.request :as mock]
            [clojure.test :refer :all]))

(deftest test-getting-channels
  (is (= (app (mock/request :get "/channels"))
         {:status  200
          :headers {"content-type" "text/plain"}
          :body    "Your expected result"})))

  ;; (testing "FIXME, I fail."
  ;;   (is (= 0 1))))
