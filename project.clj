(defproject micro_adapton "0.1.0-SNAPSHOT"
  :description "Micro Implementation of Incremental Computation in Clojure"
  :url "https://github.com/aibrahim/micro_adapton"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot micro-adapton.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
