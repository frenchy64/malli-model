(ns malli-model.ref
  (:refer-clojure :exclude [compile])
  (:require [malli-model.trace :refer [trace-ns]]))
;; R ::= {K S}           ;; registry
;; S ::= [:= v]          ;; singleton schema
;;    |  [:seqable S]    ;; seqable schema
;;    |  [:ref K]        ;; recursive reference schema
;;    |  K               ;; reference schema
(defn lookup [k R] (or (R k) (throw (ex-info (str "not in scope: " k) {}))))
(defn run [s f] (f))
(defn compile [s {:keys [R] :as o}]
  (if (contains? R s) (compile (lookup s R) o)
      (case (if (vector? s) (first s))
        := (let [[_ x] s] #(run s (fn [] (= x %))))
        :seqable (let [[_ c] s, f (compile c o)] #(run s (fn [] (and (seqable? %) (every? f %)))))
        :ref (let [[_ c] s, f (delay (compile c o))] #(run s (fn [] (@f %))))
        (throw (ex-info (str "invalid schema " (pr-str s)) {})))))
(defn validate [s o v] ((compile s o) v))
(trace-ns)
