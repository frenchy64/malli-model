(ns malli-model.trampoline-ref
  (:refer-clojure :exclude [compile])
  (:require [malli-model.trace :refer [trace-ns]]))
;; R ::= {K S}           ;; registry
;; S ::= [:= v]          ;; singleton schema
;;    |  [:seqable S]    ;; seqable schema
;;    |  [:ref K]        ;; recursive reference schema
;;    |  K               ;; reference schema
(defn lookup [k R] (or (R k) (throw (ex-info (str "not in scope: " k) {}))))
(defn run [s f] (f))
(defn compile' [s {:keys [R] :as o}]
  (if (contains? R s) (compile' (lookup s R) o)
      (case (if (vector? s) (first s))
        := (let [[_ y] s] (fn [x k] (run s #(k (= x y)))))
        :seqable (let [[_ c] s
                       f (compile' c o)]
                   (fn [x k]
                     (fn []
                       (run s
                          (fn []
                            (if (seqable? x)
                              (letfn [(check [x k]
                                        (if (empty? x)
                                          (k true)
                                          (fn []
                                            (f (first x)
                                               (fn [res]
                                                 (fn []
                                                   (if res
                                                     (run s #(check (rest x) k))
                                                     false)))))))]
                                (check (seq x) k))
                              (k false)))))))
        :ref (let [[_ c] s, f (delay (compile' c o))] (fn [x k] (run s (fn [] (@f x k)))))
        (throw (ex-info (str "invalid schema " (pr-str s)) {})))))
(defn ^:no-trace compile [s o] (let [f (compile' s o)] (fn [x] (trampoline f x identity))))
(defn validate [s o v] ((compile s o) v))
(trace-ns)
