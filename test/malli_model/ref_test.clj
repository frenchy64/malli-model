(ns malli-model.ref-test
  (:require [clojure.test :refer :all]
            [malli-model.ref :as m]
            [malli-model.trace :refer [trace-ns]]))

(def nested-validator (delay (m/compile :nest {:R {:nest [:seqable [:ref :nest]]}})))
(def red-onion? (delay (m/compile
                         :red-onion
                         {:R {:red-onion :red-layer
                              :red-layer [:seqable [:ref :white-layer]]
                              :white-layer [:seqable [:ref :red-layer]]}})))

(deftest validator-test
  (is (m/validate [:= 1] {} 1))
  (is (not (m/validate [:= 1] {} false)))
  (is (not (m/validate [:seqable [:= 1]] {} 1)))
  (is (m/validate [:seqable [:= 1]] {} [1]))
  (is (not (m/validate [:seqable [:= 1]] {} 1)))
  (is (m/validate :bar {:R {:bar [:= 1]}} 1))
  (is (not (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (not (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (m/validate :onion {:R {:onion [:seqable [:ref :onion]]}} nil))
  (is (m/validate :onion {:R {:onion [:seqable [:ref :onion]]}} [nil]))
  (is (m/validate :onion {:R {:onion [:seqable [:ref :onion]]}} [[nil]]))
  (is (m/validate :onion {:R {:onion [:seqable [:ref :onion]]}} [[[nil]]]))
  (is (m/validate :onion {:R {:onion [:seqable [:ref :onion]]}} [[[[nil]]]]))
  (is (m/validate :onion {:R {:onion [:seqable [:ref :onion]]}} [[[[[[nil]]]]]]))
  (is (m/validate :onion {:R {:onion [:seqable [:ref :onion]]}} [[[[[[1]]]]]]))
  (is (m/compile :nest {:R {:nest [:seqable [:ref :nest]]}}))
  (is (@nested-validator [[nil]]))
  (is (@nested-validator [[[nil]]]))
  (is (@nested-validator [[[[nil]]]]))
  (is (@nested-validator [[[[[nil]]]]]))
  (is (@red-onion? [nil]))
  (is (@red-onion? [[nil]]))
  (is (@red-onion? [[[[nil]]]]))
  (is (@red-onion? [[[[[nil]]]]]))
  (is (@red-onion? [[[[[[nil]]]]]]))
  )
