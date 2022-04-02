(ns rss.cli.main
  (:require [rss.tg.main :refer [sync]])
  (:gen-class))

(defn -main [& args]
  (println "Syncing...")
  (sync)
  (println "Finished"))
