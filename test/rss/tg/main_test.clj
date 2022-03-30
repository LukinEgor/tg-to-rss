(ns rss.tg.main-test
  (:require [rss.tg.main :refer [download-new-channels]]
            [rss.db.mappers.channel :as db]
            [rss.db.config :refer [db-spec]]
            [clojure.test :refer :all]))


(defn clear-db [f]
  (db/delete-all db-spec)
  (f))

(use-fixtures :each clear-db)

(def test-channel {:name "addmeto" :description nil })


(def data (download-new-channels db-spec))
data


;; (->> data (db/bulk-update-channels db-spec))
;; (db/bulk-update-channels db-spec data)

(dissoc (first data) :content)

(db/get-channels db-spec)
(db/delete-channel db-spec 28)

(defn mock-fetch-post [last-post-id]
  (fn [_ post-id]
    (if (and
         (<= post-id last-post-id)
         (not (= post-id 0))
         ) test-post test-cover-post)))

(deftest test-download-new-channels
  (db/create-channel db-spec test-channel)
  )
