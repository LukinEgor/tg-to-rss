(ns rss.core.specs.channel-spec
  (:require [clojure.spec.alpha :as s])

(s/def ::title string?)
(s/def ::description string?)
(s/def ::link string?)

(s/def ::items string?)

(s/def ::channel
  (s/keys
   :req [::title ::description ::link]
   :opt [::comment ::last-login]))
