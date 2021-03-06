(defproject puppetlabs/pcp-client "1.3.2-SNAPSHOT"
  :description "client library for PCP"
  :url "https://github.com/puppetlabs/clj-pcp-client"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0.html"}

  :pedantic? :abort

  :min-lein-version "2.7.1"

  :parent-project {:coords [puppetlabs/clj-parent "4.4.1"]
                   :inherit [:managed-dependencies]}

  :dependencies [[puppetlabs/pcp-common "1.3.1" :exclusions [org.tukaani/xz]]

                 [stylefruits/gniazdo "1.0.1" :exclusions [org.eclipse.jetty.websocket/websocket-client]]
                 ;; We only care about org.eclipse.jetty.websocket/websocket-client
                 [puppetlabs/trapperkeeper-webserver-jetty9]

                 [org.clojure/clojure]
                 [org.clojure/tools.logging]
                 [puppetlabs/ssl-utils]
                 [puppetlabs/kitchensink]
                 [prismatic/schema]
                 [puppetlabs/trapperkeeper-status]
                 [puppetlabs/trapperkeeper-scheduler]
                 [slingshot]
                 [puppetlabs/i18n]]

  :plugins [[lein-release "1.0.5" :exclusions [org.clojure/clojure]]
            [lein-parent "0.3.4"]
            [puppetlabs/i18n "0.8.0"]]

  :lein-release {:scm :git
                 :deploy-via :lein-deploy}

  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env/clojars_jenkins_username
                                     :password :env/clojars_jenkins_password
                                     :sign-releases false}]]

  :test-paths ["test" "test-resources"]

  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[puppetlabs/pcp-broker "1.5.4"]
                                  [org.clojure/tools.nrepl]
                                  [org.bouncycastle/bcpkix-jdk15on]
                                  [puppetlabs/trapperkeeper]
                                  [puppetlabs/trapperkeeper :classifier "test" :scope "test"]
                                  [puppetlabs/kitchensink :classifier "test" :scope "test"]]}
             :cljfmt {:plugins [[lein-cljfmt "0.5.7" :exclusions [org.clojure/clojure]]
                                [lein-parent "0.3.4"]]
                      :parent-project {:path "../pl-clojure-style/project.clj"
                                       :inherit [:cljfmt]}}
             :test-base [:dev
                         {:source-paths ["test-resources"]
                          :test-paths ^:replace ["test"]}]
             :test-schema-validation [:test-base
                                      {:injections [(do
                                                      (require 'schema.core)
                                                      (schema.core/set-fn-validation! true))]}]}

  :aliases {"cljfmt" ["with-profile" "+cljfmt" "cljfmt"]
            "test-all" ["with-profile" "test-base:test-schema-validation" "test"]})
