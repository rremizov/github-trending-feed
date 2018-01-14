(ns github-trending-feed.formats
  (:require [clojure.tools.logging :as logging])
  (:require [clj-rss.core :as rss]
            [muuntaja.core :as muuntaja])
  (:import (java.io ByteArrayInputStream)))

(def rss
  {:decoder nil
   :encoder
   (fn [feed charset]
     (logging/debug "Encoder;" feed charset)
     (ByteArrayInputStream. (.getBytes (apply rss/channel-xml feed) charset)))})

(def default-formats
  (muuntaja/create
   (-> muuntaja/default-options
       (assoc-in [:formats "application/rss+xml"] rss)
       (assoc :default-format "application/rss+xml"))))
