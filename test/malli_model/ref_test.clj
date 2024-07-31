(ns malli-model.ref-test
  (:require [clojure.test :refer :all]
            [malli-model.ref :as m]
            [malli-model.trace :refer [trace-ns]]))

(deftest validator-test
  (is (m/validate [:= 1] {} 1))
  (is (not (m/validate [:= 1] {} false)))
  (is (not (m/validate [:seqable [:= 1]] {} 1)))
  (is (m/validate [:seqable [:= 1]] {} [1]))
  (is (not (m/validate [:seqable [:= 1]] {} 1)))
  (is (m/validate :bar {:R {:bar [:= 1]}} 1))
  (is (not (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (not (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (m/validate :bar {:R {:bar [:seqable [:ref :bar]]}} nil))
  (is (m/validate :bar {:R {:bar [:seqable [:ref :bar]]}} [nil]))
  (is (m/validate :bar {:R {:bar [:seqable [:ref :bar]]}} [[nil]]))
  (is (m/validate :bar {:R {:bar [:seqable [:ref :bar]]}} [[[[[[nil]]]]]]))
  (is (m/validate :bar {:R {:bar [:seqable [:ref :bar]]}} [[[[[[1]]]]]] ))
  (is (m/compile :nest {:R {:nest [:seqable :nest]}}))
  )
