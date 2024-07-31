(ns malli-model.tiny
  (:refer-clojure :exclude [compile])
  (:require [malli-model.trace :refer [trace-ns]]))
(defn lookup [k R] (or (get R k) (throw (ex-info (str "not in scope: " k) {}))))
(defn run [s f] (f))
(defn compile [s {:keys [R] :as o}]
  (if (contains? R s) (compile (lookup s R) o)
      (case (first s)
        := (let [[_ x] s] #(run s (fn [] (= x %))))
        :seqable (let [[_ c] s, f (compile c o)] #(run s (fn [] (and (seqable? %) (every? f %)))))
        (throw (ex-info (str "invalid schema " (pr-str s)) {})))))
(defn validate [s o v] ((compile s o) v))
(trace-ns)
