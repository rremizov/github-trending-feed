(ns github-trending-feed.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [github-trending-feed.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[github-trending-feed started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[github-trending-feed has shut down successfully]=-"))
   :middleware wrap-dev})
