(ns malli-model.seen
  (:refer-clojure :exclude [compile])
  (:require [malli-model.trace :refer [trace-ns]]))
;; R ::= {K S}           ;; registry
;; seen ::= {K fn?}      ;; validator for refs being currently compiled
;; S ::= [:= v]          ;; singleton schema
;;    |  [:seqable S]    ;; seqable schema
;;    |  K               ;; reference schema
(defn lookup [k R] (or (R k) (throw (ex-info (str "not in scope: " k) {}))))
(defn found [k seen] (or (seen k) (throw (ex-info (str "not in scope: " k) {}))))
(defn run [s f] (f))
(defn compile [s {:keys [R seen] :as o}]
  (if (contains? R s)
    (if (contains? seen s)
      (found s seen)
      (let [p (promise)
            this (fn [x] (@p x))]
        (deliver p (compile (lookup s R) (assoc-in o [:seen s] this)))
        this))
    (case (if (vector? s) (first s))
      := (let [[_ x] s] #(run s (fn [] (= x %))))
      :seqable (let [[_ c] s, f (compile c o)] #(run s (fn [] (and (seqable? %) (every? f %)))))
      (throw (ex-info (str "invalid schema " (pr-str s)) {})))))
(defn validate [s o v] ((compile s o) v))
(trace-ns)
