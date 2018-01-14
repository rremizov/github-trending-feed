(ns github-trending-feed.feed
  (:require [clojure.string :as string]
            [clojure.tools.logging :as logging]
            [clojure.walk :refer [keywordize-keys]])
  (:require [clj-http.client :as http]
            [clj-time.coerce :as tc]
            [clj-time.core :as t]
            [net.cgrand.enlive-html :as html]))

(defrecord RSSChannel [title link description])
(defrecord RSSEntry [title link description pubDate])

(defn- fetch-github-trending [language]
  (html/html-snippet
   (:body (http/get (str "https://github.com/trending/" language "?since=today")))))

(defn- parse-title [dom]
  (-> dom (html/select [html/text-node]) string/join string/trim))

(defn- parse-titles [dom]
  (map parse-title (html/select dom [:ol.repo-list :li :h3 :a])))

(defn- parse-descriptions [dom]
  (map (comp string/trim first :content) (html/select dom [:ol.repo-list :li :div.py-1 :p])))

(defn- parse-urls [dom]
  (->> (html/select dom [:ol.repo-list :li :h3 :a])
       (map #(get-in % [:attrs :href]) )
       (map #(str "https://github.com" %))))

(defn- github-trending [language]
  (let [dom (fetch-github-trending language)]
    (map vector
         (parse-titles dom)
         (parse-urls dom)
         (parse-descriptions dom)
         (repeat (tc/to-date (t/today))))))

(defn daily [language]
  (apply vector
         (->RSSChannel (str "Github Trending Repositories: " language " Daily")
                       (str "http://localhost/daily/" language)
                       (str "Github Trending Repositories: " language " Daily"))
         (map #(apply ->RSSEntry %) (github-trending language))))
