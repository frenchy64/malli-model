(ns malli-model.trampolined-interpreter-test
  (:require [clojure.test :refer :all]
            [malli-model.trampolined-interpreter :as m]))

(deftest validator-test
  (is (m/validate [:= 1] {} 1))
  (is (not (m/validate [:= 1] {} false)))
  (is (not (m/validate [:seqable [:= 1]] {} 1)))
  (is (m/validate [:seqable [:= 1]] {} [1]))
  (is (not (m/validate [:seqable [:= 1]] {} 1)))
  (is (not (m/validate [:seqable :one] {:R {:one [:= 1]}} [1 1 1 1 1])))
  (is (m/validate :onion {:R {:onion [:= 1]}} 1))
  (is (not (m/validate :onion {:R {:onion [:= 1]}} false)))
  (is (not (m/validate :onion {:R {:onion [:= 1]}} false)))
  (is (m/validate :onion {:R {:onion [:seqable :onion]}} nil))
  (is (m/validate :onion {:R {:onion [:seqable :onion]}} [[[[[[nil]]]]]]))
  (is (not (m/validate :onion {:R {:onion [:seqable :onion]}} [[[[[[1]]]]]])))
  )
