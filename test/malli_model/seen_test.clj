(ns malli-model.seen-test
  (:require [clojure.test :refer :all]
            [malli-model.seen :as m]
            [malli-model.trace :refer [trace-ns]]))

(deftest validator-test
  (is (true? (m/validate [:= 1] {} 1)))
  (is (false? (m/validate [:= 1] {} false)))
  (is (false? (m/validate [:seqable [:= 1]] {} 1)))
  (is (true? (m/validate [:seqable [:= 1]] {} [1])))
  (is (false? (m/validate [:seqable [:= 1]] {} 1)))
  (is (true? (m/validate :bar {:R {:bar [:= 1]}} 1)))
  (is (false? (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (false? (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (true? (m/validate :bar {:R {:bar [:seqable :bar]}} nil)))
  (is (true? (m/validate :bar {:R {:bar [:seqable :bar]}} [[[[[[nil]]]]]])))
  (is (false? (m/validate :bar {:R {:bar [:seqable :bar]}} [[[[[[1]]]]]] )))
  (is (fn? (m/compile :nest {:R {:nest [:seqable :nest]}})))
  )
