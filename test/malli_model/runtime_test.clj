(ns malli-model.runtime-test
  (:require [clojure.test :refer :all]
            [malli-model.runtime :as m]))

(deftest validator-test
  (is (m/validate [:= 1] {} 1))
  (is (not (m/validate [:= 1] {} false)))
  (is (not (m/validate [:seqable [:= 1]] {} 1)))
  (is (m/validate [:seqable [:= 1]] {} [1]))
  (is (not (m/validate [:seqable [:= 1]] {} 1)))
  (is (not (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (not (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (true? (m/validate :bar {:R {:bar [:seqable :bar]}} nil)))
  (is (true? (m/validate :bar {:R {:bar [:seqable :bar]}} [[[[[[nil]]]]]])))
  )
