(ns github-trending-feed.feed
  (:require [clojure.string :as string]
            [clojure.tools.logging :as logging])
  (:require [cemerick.url]
            [clj-rss.core :as rss]
            [clj-time.coerce :as tc]
            [clj-time.format :as tf]
            [feedparser-clj.core :refer [parse-feed]]))

(defrecord RSSChannel [title link description])
(defrecord RSSEntry [title link pubDate])

(defn make-pdf-url [service-url url title published-date]
  (-> (cemerick.url/url service-url)
      (assoc :path
             (str "/pdf/"
                  (cemerick.url/url-encode url)
                  "/"
                  (cemerick.url/url-encode
                   (string/replace
                    (str (tf/unparse (tf/formatter "yyyyMMddhhmmss ")
                                     (tc/from-date published-date))
                         title)
                    #"/" "."))
                  ".pdf"))
      str))

(defn rss [service-url feed-url]
  (let [feed (parse-feed feed-url)]
    (->> feed
         :entries
         (map #(->RSSEntry
                (:title %)
                (make-pdf-url service-url
                              (:link %) (:title %) (:published-date %))
                (:published-date %)))
         (apply vector
                (->RSSChannel (:title feed)
                              (:link feed)
                              (:title feed))))))
