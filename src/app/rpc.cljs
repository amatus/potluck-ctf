(ns app.rpc
  (:require-macros
    [javelin.core :refer [defc defc=]])
  (:require
   [javelin.core]
   [castra.core :refer [mkremote]]))

(defc scoreboard nil)
(defc error nil)
(defc loading [])

(def get-scoreboard
  (mkremote 'app.api/get-scoreboard scoreboard error loading))

(defn init []
  (get-scoreboard)
  (js/setInterval get-scoreboard 1000))
