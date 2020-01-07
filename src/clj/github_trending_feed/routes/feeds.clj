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

(defapi feeds-routes
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Sample API"
                           :description "Sample Services"}}}

   :formats github-trending-feed.formats/default-formats}

  (GET "/daily/:language/" request
    :path-params [language :- s/Str]
    :return s/Any
    :summary "Github Trending repositories"
    (ok (feed/daily language))))
