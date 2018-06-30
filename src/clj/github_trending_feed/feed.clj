(ns github-trending-feed.feed
  (:require [clojure.string :as string]
            [clojure.tools.logging :as logging]
            [clojure.walk :refer [keywordize-keys]])
  (:require [clj-http.client :as http]
            [clj-time.coerce :as tc]
            [clj-time.core :as t]
            [net.cgrand.enlive-html :as html])
  (:import java.security.MessageDigest))

(defrecord RSSChannel [title link description])
(defrecord RSSEntry [title link description pubDate guid])

(defn- fetch-github-trending [language]
  (html/html-snippet
   (:body (http/get (str "https://github.com/trending/" language "?since=today")))))

(defn- parse-title [dom]
  (->> (html/select dom [:h3 :a html/text-node]) string/join string/trim))

(defn- parse-stars [dom]
  (some-> (re-seq #"(\d+) stars" (str dom)) first second Integer/parseInt))

(defn- parse-description [dom]
  (or (some->> (html/select dom [:div.py-1 :p]) first :content first string/trim)
      ""))

(defn- parse-url [dom]
  (->> (html/select dom [:h3 :a]) first :attrs :href (str "https://github.com")))

(defn- md5 [s]
  (format "%032x" (BigInteger. 1 (.digest (MessageDigest/getInstance "MD5") (.getBytes s)))))

(defn- render-guid [[title link description pubdate]]
  [{:isPermaLink false} (md5 (str title link (str pubdate)))])

(defn- github-trending [language]
  (->> (html/select (fetch-github-trending language) [:ol.repo-list :li])
       (filter #(not (nil? (parse-stars %))))
       (map (juxt
             parse-title
             parse-url
             #(string/join "\n" [(str (parse-stars %) " stars today. ") (parse-description %)])))
       (map #(conj %
                   (tc/to-date (t/plus (t/with-time-at-start-of-day (t/now))
                                       (t/seconds (parse-stars %))))))
       (map #(conj % (render-guid %)))))

(defn daily [language]
  (apply vector
         (->RSSChannel (str "Github Trending Repositories: " language " Daily")
                       (str "https://github.com/trending/" language "/?since=today")
                       (str "Github Trending Repositories: " language " Daily"))
         (map #(apply ->RSSEntry %) (github-trending language))))
