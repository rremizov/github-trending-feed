(ns github-trending-feed.routes.feeds
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.tools.logging :as logging])
  (:require [compojure.api.sweet :refer :all]
            [muuntaja.core :as muuntaja]
            [ring.util.http-response :refer :all]
            [ring.util.request :as req]
            [schema.core :as s])
  (:require [github-trending-feed.formats]
            [github-trending-feed.feed :as feed]))

(defn- absolute-url [request]
  (let [request-url (req/request-url request)
        request-path (req/path-info request)]
    (apply str (take (str/index-of request-url request-path) request-url))))

(defapi feeds-routes
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Sample API"
                           :description "Sample Services"}}}

   :formats github-trending-feed.formats/default-formats}

  (GET "/feeds/pocket/" request
       :return s/Any
       :query-params [rss-url :- String]
       :summary (str "Read URLs from Pocket RSS feed "
                     "and return RSS feed with URLs of PDFs")
       (ok (feed/rss (absolute-url request) rss-url))))
