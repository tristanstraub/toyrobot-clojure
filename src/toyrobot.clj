(ns toyrobot
  (:gen-class))

(def directions ["NORTH" "EAST" "SOUTH" "WEST"])
(def turns {"LEFT" 3 "RIGHT" 5})
(def movepairs (mapv vector directions  [[0 1] [-1 0] [0 -1] [1 0]]))
(def moves (into {} movepairs))

(defn clamp+ [a b]
  (min 4 (max 0 (+ a b))))

(def moving-commands
  ;; first dummy group is a hack, to avoid re-find returning a string, instead of a vector
  {#"()MOVE" (fn [state] (update-in state [:pos] (fn [pos] (map clamp+ pos (moves (:direction state))))))
   #"()(LEFT|RIGHT)" (fn [state turn-direction]
                       (update-in state [:direction]
                                  (fn [direction]
                                    (first (drop (turns turn-direction)
                                                 (drop-while #(not= % direction) (cycle directions)))))))
   #"()REPORT" (fn [state] (update-in state [:output]
                                      conj (str (first (:pos state)) "," (second (:pos state)) "," (:direction state))))})

(def starting-commands
  {#"()PLACE (\d),(\d),(NORTH|SOUTH|EAST|WEST)"
   (fn [state x y direction]
     (assoc state
            :pos [(read-string x) (read-string y)]
            :direction direction
            :commands (merge starting-commands moving-commands)))})

(defn process-line [state line]
  (reduce (fn [state [command-pattern command-handler]]
            (if-let [match (re-find command-pattern line)]
              ;; drop whole match and first dummy group
              (apply command-handler state (drop 2 match))
              state))
          state
          (:commands state)))

(defn run [input]
  (-> (->> (clojure.string/split input #"\n")
           (reduce process-line {:commands starting-commands}))
      (dissoc :commands)))

(defn -main []
  (doseq [line (:output (run (slurp *in*)))]
    (println line)))
