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
  (is (true?  (valid? :onion {:R {:onion [:seqable :onion]}} nil)))
  (is (true?  (valid? :onion
                      {:R {:onion :layer
                           :layer [:seqable :layer]}}
                      nil)))
  (is (true?  (valid? :onion {:R {:onion [:seqable :onion]}} [[[nil]]])))
  (is (true?  (valid? :onion {:R {:onion [:seqable :onion]}} [[[] [] [] []]])))
  (is (true?  (valid? :onion
                      {:R {:onion :layer
                           :layer [:seqable :layer]}}
                      [[[[[[[]]]]]]])))
  (is (true?  (valid? :red-onion
                      {:R {:red-onion :red-layer
                           :red-layer [:seqable :white-layer]
                           :white-layer [:seqable :red-layer]}}
                      [[[[[[nil]]]]]])))

  ;; A White Onion is a White Layer.

  ;; a White Layer is either:
  ;; - a core
  ;; - a white layer with a White Layer inside.

  ;; A Red Onion is a Red Layer.

  ;; a Red Layer is either:
  ;; - a core
  ;; - a red ring with a White Layer inside.
  ;; a White Layer is either:
  ;; - a core
  ;; - a white ring with a Red Layer inside.
  )
