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