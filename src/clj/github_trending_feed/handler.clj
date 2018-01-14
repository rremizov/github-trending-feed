(ns github-trending-feed.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [github-trending-feed.routes.feeds :refer [feeds-routes]]

            [compojure.route :as route]
            [github-trending-feed.env :refer [defaults]]
            [mount.core :as mount]
            [github-trending-feed.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    #'feeds-routes
    (route/not-found "page not found")))

(defn app [] (middleware/wrap-base #'app-routes))
