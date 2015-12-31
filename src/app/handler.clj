(ns app.handler
  (:require [castra.middleware :as castra]
            [compojure.core :as c]
            [compojure.route :as route]
            [ring.middleware.defaults :as d]
            [ring.util.response :as response]
            [simpledb.core :as db]))

(c/defroutes app-routes
  (c/GET "/" req (response/content-type (response/resource-response "index.html") "text/html"))
  (route/resources "/" {:root ""}))

(def app
  (-> app-routes
      (castra/wrap-castra 'app.api)
      (d/wrap-defaults d/api-defaults)))

(db/init)
