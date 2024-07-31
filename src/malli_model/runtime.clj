(ns malli-model.runtime
  (:require [malli-model.trace :refer [trace-ns]]))
;; R ::= {K S}           ;; registry
;; S ::= [:= v]          ;; singleton schema
;;    |  [:seqable S]    ;; seqable schema
;;    |  K               ;; reference schema
(defn lookup [k R] (or (get R k) (throw (ex-info (str "not in scope: " k) {}))))
(defn interpret [s {:keys [R] :as o} x]
  (if (contains? R s) (interpret (lookup s R) o x)
      (case (first s)
        := (let [[_ y] s] (= x y))
        :seqable (let [[_ c] s] (and (seqable? x) (every? #(interpret c o %) x)))
        (throw (ex-info (str "invalid schema " (pr-str s)) {})))))
(defn validate [s o v] (interpret s o v))
(trace-ns)
