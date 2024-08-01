(ns malli-model.trampolined-interpreter-test
  (:require [clojure.test :refer :all]
            [malli-model.trampolined-interpreter :as m]))

(deftest validator-test
  (is (m/validate [:= 1] {} 1))
  (is (not (m/validate [:= 1] {} false)))
  (is (not (m/validate [:seqable [:= 1]] {} 1)))
  (is (m/validate [:seqable [:= 1]] {} [1]))
  (is (not (m/validate [:seqable [:= 1]] {} 1)))
  (is (m/validate :bar {:R {:bar [:= 1]}} 1))
  (is (not (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (not (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (m/validate :bar {:R {:bar [:seqable :bar]}} nil))
  (is (m/validate :bar {:R {:bar [:seqable :bar]}} [[[[[[nil]]]]]]))
  (is (not (m/validate :bar {:R {:bar [:seqable :bar]}} [[[[[[1]]]]]])))
  )
