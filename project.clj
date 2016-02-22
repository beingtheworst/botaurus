(defproject sv3 "0.1.0-SNAPSHOT"
  :description "Botaurus is a genus of bitterns, a group of wading bird in the heron family Ardeidae."
  :dependencies [
                 [org.clojure/clojure "1.8.0"]
                 [metosin/compojure-api "1.0.0-RC1"  ]
                 [prismatic/schema "1.0.4" ]
                 [metosin/ring-swagger-ui "2.1.4-0"]
                 [metosin/ring-swagger "0.22.3"]
                 ]
  :ring {:handler sv3.core/app}
  :uberjar-name "botaurus.jar"
  :profiles {:dev
             {
              :dependencies [[javax.servlet/servlet-api "2.5"]]
              :plugins [[lein-ring "0.9.6"]]
              }}
  )
