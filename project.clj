(defproject rss "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [ring/ring-devel "1.6.3"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [ring/ring-mock "0.4.0"]
                 [ring/ring-json "0.5.1"]
                 [org.clojure/clojurescript "1.10.758"]
                 [clj-tagsoup/clj-tagsoup "0.3.0" :exclusions [org.clojure/clojure]]
                 [org.clojure/data.xml "0.0.8"]
                 [reagent "1.1.0"]
                 [environ "1.2.0"]
                 [figwheel-sidecar "0.5.16"]
                 [org.postgresql/postgresql "42.3.3"]
                 [com.github.seancorfield/honeysql "2.2.861"]
                 [compojure "1.6.2"]
                 [cljsjs/react "17.0.2-0"]
                 [cljsjs/react-dom "17.0.2-0"]
                 [cljs-http "0.1.46"]
                 [dev.weavejester/ragtime "0.9.1"]]
  :profiles {
             :api {
                   :plugins [[lein-ring "0.12.5"]]
                   :ring { :handler rss.api.server/app :open-browser? false }
                   }
             :cli  {
                    :dependencies [[org.clojure/tools.cli "1.0.206"]]

                    :plugins [[lein-binplus "0.6.6"]]

                    :main rss.cli.main
                    }
             }
  ;; :aot [rss.tg.main]
  :target-path "target/%s/"
  :plugins [[lein-ring "0.12.5"]
            [lein-cljsbuild "1.1.8"]
            [lein-figwheel "0.5.18"]]
  :figwheel {
             :http-server-root "public" ;; this will be in resources/
             :server-port 5309          ;; default is 3449
             :server-ip   "0.0.0.0"
             }
  :cljsbuild {
              :builds [{
                        :id "main"
                        :source-paths ["src"]
                        :figwheel true
                        :compiler {
                                   :main "rss.web.app"
                                   :asset-path "js/out"
                                   :output-to "resources/public/js/app.js"
                                   :output-dir "resources/public/js/out"}}]})
