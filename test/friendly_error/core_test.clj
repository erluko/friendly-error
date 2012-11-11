(ns friendly-error.core-test
  (:use clojure.test
        friendly-error.core))

(deftest map-test
  (def tmap map)
  (make-friend! tmap fn? coll?)
  (testing "map takes fn and col"
    (is (= [3 4] (tmap inc [2 3])))
    (is (thrown? AssertionError
                 (tmap 9 [2 3])))))

(deftest single-test
  (let [f1 (fn [a] (inc a))
        f1-f (friendly-fn f1 number?)]
  (testing "one param fn takes number"
    (is (= (f1 4) (f1-f 4)))
    (is (thrown? AssertionError
                 (f1-f 'junk))))))

(deftest multi-test
  (let [f2 (fn
             ([a]   a)
             ([a b] (+ a b)))
        f2-f (friendly-fn f2
                          [number?]
                          [number? number?])]
    (testing "one/two param fn takes number or number, number"
      (is (= (f2 4) (f2-f 4)))
      (is (= (f2 4 5) (f2-f 4 5)))
      (is (thrown? AssertionError
                   (f2-f 'junk)))
      (is (thrown? AssertionError
                   (f2-f 'junk 5)))
      (is (thrown? AssertionError
                   (f2-f 4 'junk))))))
