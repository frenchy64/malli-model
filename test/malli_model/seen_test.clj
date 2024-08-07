(ns malli-model.seen-test
  (:require [clojure.test :refer :all]
            [malli-model.seen :as m]))

(deftest validator-test
  (is (true? (m/validate [:= 1] {} 1)))
  (is (false? (m/validate [:= 1] {} false)))
  (is (false? (m/validate [:seqable [:= 1]] {} 1)))
  (is (true? (m/validate [:seqable [:= 1]] {} [1])))
  (is (false? (m/validate [:seqable [:= 1]] {} 1)))
  (is (true? (m/validate :bar {:R {:bar [:= 1]}} 1)))
  (is (false? (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (false? (m/validate :bar {:R {:bar [:= 1]}} false)))
  (is (true? (m/validate :onion {:R {:onion [:seqable :onion]}} nil)))
  (is (true? (m/validate :onion {:R {:onion [:seqable :onion]}} [[[[[[nil]]]]]])))
  (is (true? (m/validate :onion {:R {:onion [:seqable :onion]}} [[[[[[[[[nil]]]]]]]]])))
  (is (false? (m/validate :onion {:R {:onion [:seqable :onion]}} [[[[[[1]]]]]])))
  (is (fn? (m/compile :nest {:R {:nest [:seqable :nest]}})))
  (is (true?  (m/validate :red-layer
                          {:R {:red-layer [:seqable :white-layer]
                               :white-layer [:seqable :red-layer]}}
                          [[[[[[nil]]]]]])))
  (m/compile :onion
             {:R {:onion :red-layer
                  :red-layer [:seqable :white-layer]
                  :white-layer [:seqable :red-layer]}}
)
  )

[:schema {:registry {::Onion [:tuple [:maybe ::Onion]]}}
 ::Onion]

[:let {::Onion [:tuple [:maybe ::Onion]]}
 ::Onion]
[:binding {::Onion [:tuple [:maybe ::Onion]]}
 ::Onion]

[:binding {::Of :int
           ::TupleOf [:tuple ::Of]}
 [:binding {::Of :bool}
  ::TupleOf]]

(let [a 1]
  ;; lexical scope
  ;; a is in scope
  a)

(binding [a 1]
  ;; dynamic scope
  ;; a is in scope
  a)
