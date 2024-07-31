(ns malli-model.trampoline-ref-test
  (:require [clojure.test :refer :all]
            [malli-model.trampoline-ref :as m]
            [malli-model.trace :refer [trace-ns]]))

(def nested-validator (delay (m/compile :nest {:R {:nest [:seqable [:ref :nest]]}})))

(deftest validator-test
  (is (true? (m/validate [:= 1] {} 1)))
  (is (false? (m/validate [:= 1] {} false)))
  (is (false? (m/validate [:seqable [:= 1]] {} 1)))
  (is (true? (m/validate [:seqable [:= 1]] {} [1])))
  (is (false? (m/validate [:seqable [:= 1]] {} 1)))
  (is (true? (m/validate :bar {:R {:bar [:= 1]}} 1)))
  (is (false? (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (false? (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (true? (m/validate :bar {:R {:bar [:seqable [:ref :bar]]}} nil)))
  (is (true? (m/validate :bar {:R {:bar [:seqable [:ref :bar]]}} [nil])))
  (is (true? (m/validate :bar {:R {:bar [:seqable [:ref :bar]]}} [[nil]])))
  (is (true? (m/validate :bar {:R {:bar [:seqable [:ref :bar]]}} [[[[[[nil]]]]]])))
  (is (false? (m/validate :bar {:R {:bar [:seqable [:ref :bar]]}} [[[[[[1]]]]]])))
  (is (fn? (m/compile :nest {:R {:nest [:seqable [:ref :nest]]}})))
  (is (true? (@nested-validator [[nil]])))
  (is (true? (@nested-validator [[[nil]]])))
  (is (true? (@nested-validator [[[[nil]]]])))
  (is (true? (@nested-validator [[[[[nil]]]]])))
  )
