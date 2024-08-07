(ns malli-model.trampoline-test
  (:require [clojure.test :refer :all]
            [malli-model.trampoline :as m]))

(deftest validator-test
  (is (m/validate [:= 1] {} 1))
  (is (not (m/validate [:= 1] {} false)))
  (is (not (m/validate [:seqable [:= 1]] {} 1)))
  (is (m/validate [:seqable [:= 1]] {} [1]))
  (is (m/validate [:seqable [:= 1]] {} [1 1 1 1 1]))
  (is (not (m/validate [:seqable [:= 1]] {} [1 1 2 1 1])))
  (is (m/validate :bar {:R {:bar [:= 1]}} 1))
  (is (not (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (not (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (m/validate :onion {:R {:onion [:seqable :onion]}} nil))
  (is (m/validate :onion {:R {:onion [:seqable :onion]}} [[[[[[nil]]]]]]))
  (is (not (m/validate :onion {:R {:onion [:seqable :onion]}} [[[[[[1]]]]]])))
  (is (m/compile :nest {:R {:nest [:seqable :nest]}}))
  )
