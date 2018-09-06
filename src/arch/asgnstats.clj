(ns arch.asgnstats
  (:require [arch.grades :as grades]
            [clojure.pprint :as pprint]))


(defn assignment-ids [course-data]
  (map :id (get-in course-data [:data :assignments])))

(defn students [course-data]
  (-> course-data 
      :data 
      :students))


;; Architecture v3
;;
;;

(defn table-row
  "Given a set of column names and a list, converts the list into a map.

   Example: (table-row [:a :b] [0 1]) => {:a 0 :b 1}

   The backing map is an array-map to guarantee that traversal of the 
   map keys will occur in the same order as the columns.

   Example: (seq (table-row [:b :a] [1 0])) => [[:b 1] [:a 0]] 
  "
  [columns data]
  (into (array-map)
        (map (fn [[index id]] [id (nth data index)])
             (map-indexed list columns))))


(defn table [columns rows]
   (map #(table-row columns %) rows))

(defn score-table [course-data]
 (let [students    (students course-data)
       asgnids     (assignment-ids course-data)
       scores      (map :scores students)
       name-scores (map #(cons (:name %2) %1) scores students)
       columns     (cons :name asgnids)]
     (table columns name-scores)))


;; Would assess if we really need this 
;; at this point! Do we really want to support this
;; or should we just let the users have the one-liner?
(defn print-scores-table [course-data]
  (pprint/print-table (score-table course-data)))


;; Example Usage:
;; (print-scores-table grades/course-data)
