(ns malli-model.core-test
  (:require [clojure.test :refer :all]
            [malli-model.core :as m]
            [malli-model.trace :refer [trace-ns]]))

(deftest validator-test
  (is (m/validate 1 :int {}))
  (is (not (m/validate false :int {})))
  (is (m/validate false [:or :int :boolean] {}))
  (is (not (m/validate false [:or :int :boolean] {})))
  (is (not (m/validate 1 [:seqable :int] {})))
  (is ((m/validator :bar {:bar :int}) 1))
  (is (not ((m/validator :bar {:bar :int}) false)))
  (is (not ((m/validator :bar {:bar :int}) false)))
  )

(comment
  (alter-var-root #'pr-str (fn [f]
                             (fn [& xs]
                               (apply f (map (fn [x] (if (fn? x) '<fn> x)) xs)))))
  (trace-ns malli-model.core)
  (m/validator :foo {:foo [:or :int [:seqable :foo]]})
  (m/validator :foo {:foo [:or :int [:seqable [:ref :foo]]]})
  (m/validate 1 :foo {:foo [:or :int [:seqable [:ref :foo]]]})
  (m/validate [[[[1]]]] :foo {:foo [:or :int [:seqable [:ref :foo]]]})
  (m/validator [:registry {:foo [:or :int [:seqable [:ref :foo]]]}
                :foo]
               {})
  (m/validator [:registry {:sequence [:seqable :of]
                           :of :int}
                [:registry {:of :boolean}
                 :sequence]]
               {})
  (m/validator :foo {:foo :foo})
  )
