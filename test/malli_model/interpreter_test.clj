(ns malli-model.interpreter-test
  (:require [clojure.test :refer :all]
            [malli-model.interpreter :refer [valid?]]))

(deftest valid?-test
  (is (true?  (valid? [:= 1] {} 1)))
  (is (false? (valid? [:= 1] {} 2)))
  (is (false? (valid? [:seqable [:= 1]] {} 1)))
  (is (true?  (valid? [:seqable [:= 1]] {} [1])))
  (is (false? (valid? [:seqable [:= 1]] {} 1)))
  (is (false? (valid? :bar {:R {:bar [:= 1]}} false)))
  (is (false? (valid? :bar {:R {:bar [:= 1]}} false)))
  (is (true?  (valid? :bar {:R {:bar [:seqable :bar]}} nil)))
  (is (true?  (valid? :bar {:R {:bar [:seqable :bar]}} [[nil]])))
  (is (true?  (valid? :bar {:R {:bar [:seqable :bar]}} [[[] [] [] []]])))
  (is (true?  (valid? :bar {:R {:bar [:seqable :bar]}} [[[[[[nil]]]]]]))))
