(ns rss.api.sources
  (:require [rss.db.config :refer [db-spec]]
            [clojure.java.jdbc :as jdbc]
            [honey.sql :as sql]
            [honey.sql.helpers :refer :all :as h]
            [rss.core.specs.source :as source-spec]
            [rss.core.specs.tg-web-source :as tg-web-source-spec]))

(def polymorphic-tables [:tg-web-sources])

;; TODO make dynamic join
(defn get []
  (jdbc/query
   db-spec
   (-> {:select [:*],
        :from :sources,
        :left-join [:tg-web-sources [:= :sources.source-id :tg-web-sources.id]]}
       (sql/format))))

(defn- create-polymorphics-source-sql [source_type params]
  (-> (h/insert-into source_type)
      (h/values params)
      (sql/format)))

(defn- create-source-sql [params]
  (-> (h/insert-into :sources)
      (h/values params)
      (sql/format)))

(defn post [source params]
  (if (and
       (source-spec/valid? source)
       (tg-web-source-spec/valid? params)) ;TODO make dynamic
    (jdbc/with-db-transaction [conn db-spec]
      (let [{ id :id } (jdbc/execute!
                        conn
                        (create-polymorphics-source-sql (:source-type source) [params])
                        { :return-keys true })]
        (jdbc/execute!
         conn
        (create-source-sql [(conj source { :source-id id })])
         { :return-keys true })))))
