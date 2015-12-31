(ns app.api
  (:require [castra.core :refer [defrpc]]
            [simpledb.core :as db]))

(defrpc get-scoreboard []
        (db/get :scoreboard))

(defrpc check-token [token]
        {:rpc/pre (contains? (db/get :tokens) token)}
        true)

(defrpc set-name! [token name]
        {:rpc/pre (contains? (db/get :tokens) token)}
        (db/update! :scoreboard
                    assoc-in
                    [(first (.split token "-")) :name]
                    (apply str (take 32 name))))

(defrpc submit-flag! [token flag]
        {:rpc/pre (and (contains? (db/get :tokens) token)
                       (contains? (db/get :flags) flag))}
        (let [problem (db/get-in :flags [flag])]
          (db/update! :scoreboard
                      assoc-in
                      [(first (.split token "-")) :scores problem]
                      :solved)))

(defrpc admin-eval! [token expr]
        {:rpc/pre (= token (db/get :admin))}
        (binding [*ns* (find-ns 'simpledb.core)]
          (eval (read-string expr))))
