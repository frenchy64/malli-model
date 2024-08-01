(ns malli-model.tiny
  (:refer-clojure :exclude [compile])
  (:require [malli-model.trace :refer [trace-ns]]))
;; R ::= {K S}           ;; registry
;; S ::= [:= v]          ;; singleton schema
;;    |  [:seqable S]    ;; seqable schema
;;    |  K               ;; reference schema
(defn lookup [k R] (or (get R k) (throw (ex-info (str "not in scope: " k) {}))))
(defn run [s f] (f))
(defn compile [s {:keys [R] :as o}]
  (if (contains? R s)
    (compile (lookup s R) o)
    (case (first s)
      := (let [[_ x] s] #(run s (fn [] (= x %))))
      :seqable (let [[_ c] s, f (compile c o)] #(run s (fn [] (and (seqable? %) (every? f %)))))
      (throw (ex-info (str "invalid schema " (pr-str s)) {})))))
(defn validate [s o x] ((compile s o) x))
(trace-ns)
