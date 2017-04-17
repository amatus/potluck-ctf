(set-env!
  :dependencies '[[adzerk/boot-cljs          "1.7.228-2"]
                  [adzerk/boot-cljs-repl     "0.3.3"]
                  [compojure                 "1.5.1"]
                  [hoplon/castra             "3.0.0-alpha5"]
                  [hoplon                    "6.0.0-alpha17"]
                  [org.clojure/clojure       "1.7.0"]
                  [org.clojure/clojurescript "1.7.170"]
                  [pandeiro/boot-http        "0.7.6"]
                  [ring                      "1.5.0"]
                  [ring/ring-defaults        "0.3.0-beta1"]
                  [simpledb                  "0.1.4"]]
  :source-paths   #{"src"}
  :resource-paths #{"assets"})

(require
  '[adzerk.boot-cljs      :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
  '[hoplon.boot-hoplon    :refer [hoplon prerender]]
  '[pandeiro.boot-http    :refer [serve]])

(deftask dev
  "Build potluck-ctf for local development."
  []
  (comp
    (serve
      :handler 'app.handler/app
      :reload true
      :port 8000)
    (watch)
    (speak)
    (hoplon)
    (cljs-repl)
    (cljs)))

(deftask prod
  "Build potluck-ctf for production deployment."
  []
  (comp
    (hoplon)
    (cljs :optimizations :advanced)
    (prerender)
    (aot :all true)
    (web :serve 'app.handler/app)
    (uber)
    (war :file "potluck.war")
    (target :dir #{"target"})))

;; vim: set expandtab ts=2 sw=2 filetype=clojure :
