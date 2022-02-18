(defproject rss "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [nrepl "0.3.1"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [ring/ring-devel "1.6.3"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [environ "1.2.0"]
                 [org.postgresql/postgresql "42.3.3"]
                 [com.github.seancorfield/honeysql "2.2.861"]
                 [compojure "1.6.2"]
                 [dev.weavejester/ragtime "0.9.1"]]
  :ring { :handler rss.api.server/app :open-browser? false }
  :plugins [[lein-ring "0.12.5"]])
