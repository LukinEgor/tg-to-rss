(ns rss.core.specs.tg-web-source
  (:require [clojure.spec.alpha :as s]))

(defn valid_state? [value] (contains? #{"new" "syncing" "finished" "failed"} value))

(s/def ::channel-name string?)
(s/def ::description string?)
(s/def ::last-post-id number?)
(s/def ::state (s/and string? valid_state?))

(s/def ::tg-web-source
  (s/keys
   :req-un [::channel-name ::description
            ::last-post-id ::state]))

(defn valid? [source] (s/valid? ::tg-web-source source))
