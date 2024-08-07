(ns malli-model.tiny-test
  (:require [clojure.test :refer :all]
            [malli-model.tiny :refer [valid?]]))

(deftest validator-test
  (is (true?  (valid? [:= 1] {} 1)))
  (is (false? (valid? [:= 1] {} false)))
  (is (false? (valid? [:seqable [:= 1]] {} 1)))
  (is (true?  (valid? [:seqable [:= 1]] {} [1])))
  (is (false? (valid? 1 [:seqable [:= 1]] {})))
  (is (true?  ((m/compile :bar {:R {:bar [:= 1]}}) 1)))
  (is (false? (valid? :bar {:R {:bar [:= 1]}} false)))
  (is (false? (valid? :bar {:R {:bar [:= 1]}} false)))
  (is (thrown? StackOverflowError (m/compile :bar {:R {:bar [:seqable :bar]}})))
  (is (thrown? StackOverflowError (m/compile :onion {:R {:onion [:seqable :onion]}})))
  (is (thrown? StackOverflowError (m/compile :onion {:R {:onion [:seqable :onion]}})))
  (is (true?  (m/compile :onion
                         {:R {:onion :red-layer
                              :red-layer [:seqable :white-layer]
                              :white-layer [:seqable :red-layer]}}
)))
  )
