(ns rss.core.specs.source
  (:require [clojure.spec.alpha :as s]))

(defn- valid_type? [value] (contains? #{"tg_web_sources"} value))

(s/def ::source-type (s/and string? valid_type?))
(s/def ::source-id number?)

(s/def ::source
  (s/keys
   :req-un [::source-type]
   :opt-un [::source-id]))

(defn valid? [source] (s/valid? ::source source))
