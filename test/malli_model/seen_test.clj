(ns malli-model.seen-test
  (:require [clojure.test :refer :all]
            [malli-model.seen :as m]
            [malli-model.trace :refer [trace-ns]]))

(comment
  (trace-ns malli-model.seen)
  )

(deftest validator-test
  (is (m/validate 1 [:= 1] {}))
  (is (not (m/validate false [:= 1] {})))
  (is (not (m/validate 1 [:seqable [:= 1]] {})))
  (is (m/validate [1] [:seqable [:= 1]] {}))
  (is (not (m/validate 1 [:seqable [:= 1]] {})))
  (is (m/validate :bar {:R {:bar [:= 1]}} 1))
  (is (not (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (not (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (m/validate :bar {:R {:bar [:seqable :bar]}} nil))
  (is (m/validate :bar {:R {:bar [:seqable :bar]}} [[[[[[nil]]]]]]))
  (is (m/validate :bar {:R {:bar [:seqable :bar]}} [[[[[[1]]]]]] ))
  (is (m/compile :nest {:R {:nest [:seqable :nest]}}))
  )
