(ns malli-model.trampolined-interpreter
  (:require [malli-model.trace :refer [trace-ns]]))
;; R ::= {K S}           ;; registry
;; S ::= [:= v]          ;; singleton schema
;;    |  [:seqable S]    ;; seqable schema
;;    |  K               ;; reference schema
(defn lookup [k R] (or (get R k) (throw (ex-info (str "not in scope: " k) {}))))
(defn interpret [s {:keys [R] :as o} x k]
  (if (contains? R s) (interpret (lookup s R) o x k)
      (case (if (vector? s) (first s))
        := (let [[_ y] s] (k (= x y)))
        :seqable (let [[_ c] s]
                   (if (seqable? x)
                     (letfn [(check [x k]
                               (if (empty? x)
                                 (k true)
                                 (fn []
                                   (interpret c o (first x)
                                              (fn [res]
                                                (if res
                                                  (fn [] (check (rest x) k))
                                                  (k false)))))))]
                       (check (seq x) k))
                     (k false)))
        (throw (ex-info (str "invalid schema " (pr-str s)) {})))))
(defn validate [s o v] (trampoline (fn [] (interpret s o v identity))))
(trace-ns)
