(ns app.rpc
  (:require-macros
    [javelin.core :refer [defc defc=]])
  (:require
   [javelin.core]
   [castra.core :refer [mkremote]]))

(defc scoreboard nil)
(defc scoreboard-error nil)
(defc error nil)
(defc loading [])
(defc token-ok false)
(defc eval-result nil)
(defc hints "No hints")

(def get-scoreboard
  (mkremote 'app.api/get-scoreboard scoreboard scoreboard-error loading))

(def check-token
  (mkremote 'app.api/check-token token-ok error loading))

(def set-name!
  (mkremote 'app.api/set-name! scoreboard error loading))

(def submit-flag!
  (mkremote 'app.api/submit-flag! scoreboard error loading))

(def admin-eval!
  (mkremote 'app.api/admin-eval! eval-result error loading))

(def get-hints
  (mkremote 'app.api/get-hints hints error loading))

(defn init []
  (get-scoreboard)
  (js/setInterval get-scoreboard 1000))
