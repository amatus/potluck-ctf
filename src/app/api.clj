(ns app.api
  (:require [castra.core :refer [defrpc]]
            [simpledb.core :as db]))

(defrpc get-scoreboard []
        (db/get :scoreboard))
