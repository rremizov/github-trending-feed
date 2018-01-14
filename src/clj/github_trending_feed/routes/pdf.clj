(ns github-trending-feed.routes.pdf
  (:require [clojure.string :as string]
            [clojure.tools.logging :as logging])
  (:require [cemerick.url]
            [clj-http.client :as http]
            [compojure.core :refer :all]
            [ring.util.response :as response]
            [ring.wicked.pdf :as wicked]))

(def ^:private pdf-source-header
  (str "<!DOCTYPE html><html lang=\"en\"><body>"
       "Source: %s"
       "</body></html>"))

(defn- make-pdf [url]
  (wicked/as-pdf (str (format pdf-source-header url)
                      (:body (http/get url)))))

(defn- write-response [input-stream]
  (-> (response/response input-stream)
      (response/content-type "application/pdf")))

(defn- url->pdf [{:keys [params]}]
  (if (or (not (:article-url params))
          (not (:title params)))
    {:status 400
     :headers {"Content-Type" "text/html"}
     :body ""}
    (write-response
     (make-pdf (cemerick.url/url-decode (:article-url params))))))

(defroutes pdf-routes
  (GET "/pdf/:article-url/:title" request (url->pdf request)))
