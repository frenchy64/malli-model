(ns malli-model.tiny-test
  (:require [clojure.test :refer :all]
            [malli-model.tiny :as m]))

(deftest validator-test
  (is (m/validate [:= 1] {} 1))
  (is (not (m/validate [:= 1] {} false)))
  (is (not (m/validate [:seqable [:= 1]] {} 1)))
  (is (m/validate [:seqable [:= 1]] {} [1]))
  (is (not (m/validate 1 [:seqable [:= 1]] {})))
  (is ((m/compile :bar {:R {:bar [:= 1]}}) 1))
  (is (not (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (not (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (thrown? StackOverflowError (m/compile :bar {:R {:bar [:seqable :bar]}})))
  )
