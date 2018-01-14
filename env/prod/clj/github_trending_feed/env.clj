(ns github-trending-feed.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[github-trending-feed started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[github-trending-feed has shut down successfully]=-"))
   :middleware identity})
