(ns arch.asgnstats-test
  (:require [clojure.test :refer :all]
            [arch.asgnstats :refer :all]))

(deftest table-row-test
  (testing "Correct creation of table rows"
    (is (= {:a 1 :b 2} 
           (table-row [:a :b] [1 2]))) 
    (is (= {:a 1 :b 2 :c [100]} 
           (table-row [:a :b :c] [1 2 [100]]))) 
    (is (= {} 
           (table-row [] [])))                
    (is (= {} 
           (table-row nil nil))))) 

(deftest table-test 
  (testing "Correct creation of tables from rows/cols"
    (is (= [{:a 1 :b 2} {:a 3 :b 4}]
           (table [:a :b] [[1 2] [3 4]])))
    (is (= [{:a 1 :b 2} {:a 3 :b 4}]
           (table [:a :b] [[1 2] [3 4 100 200 400]])))
    (is (= []
           (table [] [])))))

;; ... you get the idea for how we would be able to test
;; score-table now
