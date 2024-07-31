(ns malli-model.trampoline
  (:refer-clojure :exclude [compile])
  (:require [malli-model.trace :refer [trace-ns]]))
(defn lookup [k R] (or (R k) (throw (ex-info (str "not in scope: " k) {}))))
(defn found [k seen] (or (seen k) (throw (ex-info (str "not in scope: " k) {}))))
(defn run [s f] (f))
(defn compile' [s {:keys [R seen] :as o}]
  (if (contains? R s)
    (if (contains? seen s)
      (found s seen)
      (let [p (promise)
            this (fn [x k] (@p x k))]
        (deliver p (compile' (lookup s R) (assoc-in o [:seen s] this)))
        this))
    (case (if (vector? s) (first s))
      := (let [[_ y] s] (fn [x k] (run s #(k (= x y)))))
      :seqable (let [[_ c] s
                     f (compile' c o)]
                 (fn [x k]
                   (if (seqable? x)
                     (letfn [(check [x k]
                               (run s
                                    #(if (empty? x)
                                       (k true)
                                       (f (first x)
                                          (fn [res]
                                            (if res
                                              (check (rest x) k)
                                              false))))))]
                       (check (seq x) k))
                     (k false))))
      (throw (ex-info (str "invalid schema " (pr-str s)) {})))))
(defn ^:no-trace compile [s o] (let [f (compile' s o)] (fn [x] (trampoline f x identity))))
(defn validate [s o v] ((compile s o) v))
(trace-ns)
