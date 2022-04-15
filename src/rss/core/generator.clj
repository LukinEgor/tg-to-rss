(ns rss.core.generator
  (:require [clojure.data.xml :refer :all]))

(defn channel-xml [channel items]
  (emit-str
   (element :channel {}
            (element :title {} (:title channel))
            (element :link {} (:link channel))
            (element :description {} (:description channel))
            (map
             (fn [item]
               (element :item {}
                        (element :title {} (:title item))
                        (element :link {} (:link item))
                        (element :description {} (:description item)))) items))))

