(ns malli-model.core)

(defn lookup [k R]
  {:post [(some? %)]}
  (R k))

(declare validator)

(defmulti -validator (fn [s R] (if (keyword? s) s (first s))))
; :nil, :int, :boolean
(defmethod -validator :nil [_nil_ R] nil?)
(defmethod -validator :int [_int_ R] integer?)
(defmethod -validator :boolean [_boolean_ R] boolean?)

; [:enum v1 v2 v3 ...]
(defmethod -validator :enum [[_enum_ & vals] R]
  (let [vset (set vals)]
    (fn [x]
      (contains? vset x))))

; [:= v]
(defmethod -validator :enum [[_=_ v] R]
  (fn [x] (= v x)))

; [:or S1 S2 S3 ...]
(defmethod -validator :or [[_or_ & ss] R]
  (let [vs (mapv #(validator % R) ss)]
    (fn [x]
      (boolean
        (some #(% x) vs)))))

; [:seqable S]
(defmethod -validator :seqable [[_seqable_ s] R]
  (let [v (validator s R)]
    (fn [x]
      (and (seqable? x)
           (every? v x)))))

; [:registry R S]
(defmethod -validator :registry [[_registry_ R' s] R]
  (validator s (merge R R')))

; [:ref K]
(defmethod -validator :ref [[_ref_ K] R]
  (let [s (lookup K R)]
    (fn [x]
      ((validator s R) x))))

(defmethod -validator :default [s R]
  (throw (ex-info (str "Invalid schema: " (pr-str s))
                  {:schema s})))

(defn validator [s R]
  (if (contains? R s)
    (validator (lookup s R) R)
    (-validator s R)))

(defn validate [v s R] ((validator s R) v))
