(ns malli-model.seen)

(defn lookup [k R] (or (R k) (throw (ex-info (str "not in scope: " k) {}))))
(defn found [k seen] (or (seen k) (throw (ex-info (str "not in scope: " k) {}))))
(defn validator [s {:keys [R seen] :as o}]
  (if (contains? R s)
    (if (contains? seen s)
      (found s seen)
      (let [p (promise)
            this (fn [x] (@p x))]
        (deliver p (validator (lookup s R) (assoc-in o [:seen s] this)))
        this))
    (case (if (vector? s) (first s))
      := (let [[_ x] s] #(= x %))
      :seqable (let [[_ s] s, v (validator s o)] #(and (seqable? %) (every? v %)))
      (throw (ex-info (str "invalid schema " (pr-str s)) {})))))
(defn validate [v s o] ((validator s o) v))
