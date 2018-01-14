(ns user
  (:require [mount.core :as mount]
            github-trending-feed.core))

(defn start []
  (mount/start-without #'github-trending-feed.core/repl-server))

(defn stop []
  (mount/stop-except #'github-trending-feed.core/repl-server))

(defn restart []
  (stop)
  (start))
