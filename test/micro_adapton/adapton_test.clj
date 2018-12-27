(ns micro-adapton.adapton-test
  (:require [clojure.test :refer :all]
            [micro-adapton.adapton :refer :all]))

(deftest adapton-compute-test
  (let [r1 (adapton-ref 10)
        r2 (adapton-ref 8)
        a (mk-athunk (fn [] (- (adapton-compute r1) (adapton-compute r2))))
        _ (adapton-add-dcg-edge! a r1)
        _ (adapton-add-dcg-edge! a r2)]
    (is (= (adapton-compute a) 2))
    (adapton-ref-set! r1 20)
    (is (= (adapton-compute a) 12))))








