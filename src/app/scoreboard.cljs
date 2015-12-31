(ns app.scoreboard
  (:require [hoplon.core :refer [table tbody td th thead tr]])
  (:require-macros [hoplon.core :refer [defelem loop-tpl]]
                   [javelin.core :refer [cell=]]))

(defelem scoreboard
  [{:keys [scoreboard] :as attr} kids]
  (let [scoreboard (cell= (merge (sorted-map) scoreboard))
        problems (cell= (mapcat (fn [[id person]]
                                  (map (partial vector id) (:problems person)))
                                scoreboard))
        scores (cell= (reverse (sort
         (map (fn [[id person]]
                [(apply
                   +
                   (concat
                     (map (fn [[prob state]]
                            (if (and
                                  (= :solved state)
                                  (not (contains? (:problems person) prob)))
                              1
                              0))
                          (:scores person))
                     (map (fn [prob]
                            (if (some (fn [[id2 person2]]
                                        (and
                                          (= :solved
                                             (get (:scores person) prob))
                                          (= :solved
                                             (get (:scores person2) prob))
                                          (not= id id2)))
                                      scoreboard)
                              1
                              0))
                          (:problems person))))
                 id])
              scoreboard))))]
    (table
      (thead
        (tr
          (th :colspan 2
              :style "border:none")
          (loop-tpl :bindings [probs (cell= (partition-by first problems))]
                    (th :text (cell= (:name (get scoreboard
                                                 (first (first probs)))))
                        :colspan (cell= (count probs)))))
        (tr
          (th "Player")
          (th "Score")
          (loop-tpl :bindings [[owner name] problems]
                    (th :text name))))
      (tbody
        (loop-tpl :bindings [[score id] scores]
                  (let [player (cell= (get scoreboard id))]
                    (tr
                      (td :text (cell= (:name player)))
                      (td :text score)
                      (loop-tpl :bindings [[owner _name] problems]
                                (td :text (cell=
                                            (name (get (:scores player)
                                                       _name
                                                       :unsolved))))))))))))

;; vim: set expandtab ts=2 sw=2 filetype=clojure :
