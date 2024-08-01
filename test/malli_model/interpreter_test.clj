(ns malli-model.interpreter-test
  (:require [clojure.test :refer :all]
            [malli-model.interpreter :as m]))

(deftest validator-test
  (is (true? (m/valid? [:= 1] {} 1)))
  (is (false? (m/valid? [:= 1] {} false)))
  (is (false? (m/valid? [:seqable [:= 1]] {} 1)))
  (is (true? (m/valid? [:seqable [:= 1]] {} [1])))
  (is (false? (m/valid? [:seqable [:= 1]] {} 1)))
  (is (false? (m/valid? :bar {:R {:bar [:= 1]}} false)))
  (is (false? (m/valid? :bar {:R {:bar [:= 1]}} false)))
  (is (true? (m/valid? :bar {:R {:bar [:seqable :bar]}} nil)))
  (is (true? (m/valid? :bar {:R {:bar [:seqable :bar]}} [[nil]])))
  (is (true? (m/valid? :bar {:R {:bar [:seqable :bar]}} [[[] [] [] []]])))
  (is (true? (m/valid? :bar {:R {:bar [:seqable :bar]}} [[[[[[nil]]]]]]))))
