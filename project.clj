(defproject github-trending-feed "0.1.0-SNAPSHOT"

  :description "RSS feed for github.com/trending"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :url "https://github.com/rremizov/github-trending-feed"

  :dependencies [[bouncer "1.0.1"]
                 [cheshire "5.10.0"]
                 [cider/cider-nrepl "0.25.4"]
                 [clj-http "3.10.3"]
                 [clj-rss "0.2.6"]
                 [clj-time "0.15.2"]
                 [com.cemerick/url "0.1.1"]
                 [compojure "1.6.2"]
                 [cprop "0.1.17"]
                 [enlive "1.1.6"]
                 [luminus-immutant "0.2.5"]
                 [luminus-nrepl "0.1.7"]
                 [markdown-clj "1.10.5"]
                 [metosin/compojure-api "2.0.0-alpha16"]
                 [metosin/ring-http-response "0.9.1"]
                 [mount "0.1.16"]
                 [org.clojars.scsibug/feedparser-clj "0.4.0"]
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/tools.cli "1.0.194"]
                 [org.clojure/tools.logging "1.1.0"]
                 [org.webjars.bower/tether "2.0.0-beta.5"]
                 [org.webjars/bootstrap "4.5.3"]
                 [org.webjars/font-awesome "5.15.1"]
                 [org.webjars/jquery "3.5.1"]
                 [org.webjars/webjars-locator-jboss-vfs "0.1.0"]
                 [ring-middleware-format "0.7.4"]
                 [ring-webjars "0.2.0"]
                 [ring-wicked-pdf "0.4.2"]
                 [ring/ring-defaults "0.3.2"]
                 [selmer "1.12.31"]]

  :min-lein-version "2.0.0"

  :jvm-opts ["-server" "-Dconf=.lein-env"]
  :source-paths ["src/clj"]
  :resource-paths ["resources"]
  :target-path "target/%s/"
  :main github-trending-feed.core

  :plugins [[lein-cprop "1.0.1"]
            [lein-immutant "2.1.0"]]

  :profiles
  {:uberjar {:omit-source true
             :aot :all
             :uberjar-name "github-trending-feed.jar"}

   :prod {:source-paths ["env/prod/clj"]
          :resource-paths ["env/prod/resources"]}

   :dev           [:project/dev :profiles/dev]
   :test          [:project/dev :project/test :profiles/test]

   :project/dev  {:dependencies [[prone "2020-01-17"]
                                 [ring/ring-mock "0.4.0"]
                                 [ring/ring-devel "1.8.2"]
                                 [pjstadig/humane-test-output "0.10.0"]]
                  :plugins      [[com.jakemccrary/lein-test-refresh "0.14.0"]
                                 [lein-cljfmt "0.6.4"]]

                  :source-paths ["env/dev/clj" "test/clj"]
                  :resource-paths ["env/dev/resources"]
                  :repl-options {:init-ns user}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]}
   :project/test {:resource-paths ["env/test/resources"]}
   :profiles/dev {}
   :profiles/test {}})
