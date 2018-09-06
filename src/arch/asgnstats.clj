(ns arch.asgnstats
  (:require [arch.grades :as grades]
            [clojure.pprint :as pprint]))


(defn assignment-ids [course-data]
  (map :id (get-in course-data [:data :assignments])))

(defn students [course-data]
  (-> course-data 
      :data 
      :students))


;; Architecture v2
;;
;;

(defn table-row
  "Given a set of column names and a list, converts the list into a map.

   Example: (table-row [:a :b] [0 1]) => {:a 0 :b 1}

  "
  [columns data]
  (reduce (fn [m [index id]] (assoc m id (nth data index)))
          {}
          (map-indexed list columns)))

(defn print-scores-table [course-data]
  (let [students    (students course-data)
        asgnids     (assignment-ids course-data)
        scores      (map :scores students)
        name-scores (map #(cons (:name %2) %1) scores students)
        columns     (cons :name asgnids)
        table-data  (map #(table-row columns %) name-scores)]
    (pprint/print-table columns table-data)))



;; Example Usage:
(print-scores-table grades/course-data)
