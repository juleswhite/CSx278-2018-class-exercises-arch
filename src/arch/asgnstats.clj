(ns arch.asgnstats
  (:require [arch.grades :as grades]
            [clojure.pprint :as pprint]))


(defn assignment-ids [course-data]
  (map :id (get-in course-data [:data :assignments])))

(defn students [course-data]
  (-> course-data 
      :data 
      :students))


;; Architecture v1
;;
;; 

(defn assignment-indexes [course-data]
  (take (count (assignment-ids course-data)) (range 0 10000)))

(defn assignment-score [student assignment-index]
  (nth (:scores student) assignment-index))

(defn assignment-scores-by-id [course-data student]
  (let [ids  (assignment-ids course-data)
        idxs (assignment-indexes course-data)]
    (map (fn [asgn-idx] 
             [(nth ids asgn-idx) 
              (assignment-score student asgn-idx)])    
         (assignment-indexes course-data))))

(defn assignment-scores-lookup [course-data student]
  (assoc
    (into {}  (assignment-scores-by-id course-data student))
    :name (:name student)))

(defn print-scores-table [course-data]
  (let [data (map #(assignment-scores-lookup course-data %) (students course-data))]
     (pprint/print-table (cons :name (assignment-ids course-data)) data)))


;; Example Usage:
(print-scores-table grades/course-data)
