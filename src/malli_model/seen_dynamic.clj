(ns malli-model.seen-dynamic
  (:refer-clojure :exclude [compile])
  (:require [malli-model.trace :refer [trace-ns]]))
;; R ::= {K S}           ;; registry
;; seen ::= {K fn?}      ;; validator for refs being currently compiled
;; S ::= [:= v]          ;; singleton schema
;;    |  [:seqable S]    ;; seqable schema
;;    |  [:binding R S]  ;; dynamic binding schema
;;    |  K               ;; reference schema
(defn lookup [k R] (or (R k) (throw (ex-info (str "not in scope: " k) {}))))
(defn ^:no-trace seen-key [s R] [s R])
(defn found [k {:keys [R seen]}] (or (seen (seen-key k R)) (throw (ex-info (str "not in scope: " k) {}))))
(defn run [s f] (f))
(defn compile [s {:keys [R seen] :as o}]
  (if (contains? R s)
    (if (contains? seen (seen-key s R))
      (found s o)
      (let [p (promise)
            this (fn [x] (@p x))]
        (deliver p (compile (lookup s R) (assoc-in o [:seen (seen-key s R)] this)))
        this))
    (case (if (vector? s) (first s))
      := (let [[_ x] s] #(run s (fn [] (= x %))))
      :seqable (let [[_ c] s, f (compile c o)] #(run s (fn [] (and (seqable? %) (every? f %)))))
      :binding (let [[_ R' s] s] (compile s (update o :R merge R')))
      (throw (ex-info (str "invalid schema " (pr-str s)) {})))))
(defn validate [s o v] ((compile s o) v))
(trace-ns)
