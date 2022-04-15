(ns rss.api.rss.channels
  (:require [rss.db.config :refer [db-spec]]
            [clojure.java.jdbc :as jdbc]
            [honey.sql :as sql]
            [honey.sql.helpers :refer :all :as h]
            [rss.core.generator :refer [channel-xml]]
            [ring.util.response :refer [response]]
            [rss.db.mappers.channel :as mapper]))

(defn show [channel-id]
  (let [items (jdbc/query
                 db-spec
                 (-> {:select [:*],
                      :from :items,
                      :where [:= :channel-id channel-id]}
                     (sql/format)))
        channel (jdbc/query
                 db-spec
                 (-> {:select [:*],
                      :from :channels,
                      :where [:= :id channel-id]}
                     (sql/format)))]
    (channel-xml channel items)))
