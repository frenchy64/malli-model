(ns malli-model.dynamic-test
  (:require [clojure.test :refer :all]
            [malli-model.dynamic :as m]))

(deftest validator-test
  (is (true? (m/validate [:= 1] {} 1)))
  (is (false? (m/validate [:= 1] {} false)))
  (is (false? (m/validate [:seqable [:= 1]] {} 1)))
  (is (true? (m/validate [:seqable [:= 1]] {} [1])))
  (is (false? (m/validate 1 [:seqable [:= 1]] {})))
  (is (true? ((m/compile :bar {:R {:bar [:= 1]}}) 1)))
  (is (false? (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (false? (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (false? (m/validate [:binding {:bar [:= 1]} :bar] {} false)))
  (is (false?
        (m/validate [:binding {:ones-coll [:seqable :one]
                               :one [:= 1]}
                     [:binding {:one [:= 2]}
                      :ones-coll]]
                    {}
                    [1])))
  (is (true?
        (m/validate [:binding {:ones-coll [:seqable :one]
                               :one [:= 1]}
                     [:binding {:one [:= 2]}
                      :ones-coll]]
                    {}
                    [2])))
  (is (thrown? StackOverflowError (m/compile :bar {:R {:bar [:seqable :bar]}}))))
