(ns malli-model.tiny-test
  (:require [clojure.test :refer :all]
            [malli-model.tiny :as m :refer [valid?]]))

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
  )
