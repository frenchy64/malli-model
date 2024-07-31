(ns malli-model.seen-dynamic-test
  (:require [clojure.test :refer :all]
            [malli-model.seen-dynamic :as m]))

(deftest validator-test
  (is (true? (m/validate [:= 1] {} 1)))
  (is (false? (m/validate [:= 1] {} false)))
  (is (false? (m/validate [:seqable [:= 1]] {} 1)))
  (is (true? (m/validate [:seqable [:= 1]] {} [1])))
  (is (false? (m/validate [:seqable [:= 1]] {} 1)))
  (is (true? (m/validate :bar {:R {:bar [:= 1]}} 1)))
  (is (false? (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (false? (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (true? (m/validate :bar {:R {:bar [:seqable :bar]}} nil)))
  (is (true? (m/validate :bar {:R {:bar [:seqable :bar]}} [[[[[[nil]]]]]])))
  (is (false? (m/validate :bar {:R {:bar [:seqable :bar]}} [[[[[[1]]]]]] )))
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
  (is (fn? (m/compile :nest {:R {:nest [:seqable :nest]}})))
  )
