(ns malli-model.seen-test
  (:require [clojure.test :refer :all]
            [malli-model.seen :as m]
            [malli-model.trace :refer [trace-ns]]))

(comment
  (trace-ns malli-model.seen)
  )

(deftest validator-test
  (is (m/validate 1 [:= 1] {}))
  (is (not (m/validate false [:= 1] {})))
  (is (not (m/validate 1 [:seqable [:= 1]] {})))
  (is (m/validate [1] [:seqable [:= 1]] {}))
  (is (not (m/validate 1 [:seqable [:= 1]] {})))
  (is ((m/validator :bar {:R {:bar [:= 1]}}) 1))
  (is (not ((m/validator :bar {:R {:bar [:= 1]}}) false)))
  (is (not ((m/validator :bar {:R {:bar [:= 1]}}) false)))
  (is (m/validate nil :bar {:R {:bar [:seqable :bar]}}))
  (is (m/validate [[[[[[nil]]]]]] :bar {:R {:bar [:seqable :bar]}}))
  (is (m/validate [[[[[[1]]]]]] :bar {:R {:bar [:seqable :bar]}}))
  )
