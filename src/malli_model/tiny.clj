(ns malli-model.tiny)

(defn lookup [R k] (or (get R k) (throw (ex-info (str "not in scope: " k) {}))))
(defn validator [s {:keys [R] :as o}]
  (if (contains? R s) (validator (lookup R s) o)
      (case (first s)
        := (let [[_ x] s] #(= x %))
        :seqable (let [[_ s] s, v (validator s o)] #(and (seqable? %) (every? v %)))
        (throw (ex-info (str "invalid schema " (pr-str s)) {})))))


;:registry (let [[_ R' s] s] (validator s (update o :R merge R')))
;:ref (let [[_ k] s] #((validator (lookup R s) o) %))
