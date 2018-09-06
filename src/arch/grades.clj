(ns arch.grades)


(def course-data {:data {:students    [{:name "Jill" :scores [98 100]}
                                       {:name "Jim" :scores [95 97]}]
                         :assignments [{:id :asgn1 :deadline "2020/08/30"}
                                       {:id :asgn2 :deadline "2020/09/18"}]}})


(defn find-student [course-data student-name]
  (first 
    (filter 
       #(= student-name (:name %)) 
       (get-in course-data [:data :students]))))
            

(defn assignment-scores [course-data student-name]
  (:scores (find-student course-data student-name)))


(defn course-grade [course-data student-name]
  (let [scores (:scores (find-student course-data student-name))
        total  (reduce + scores)]
     (/ total (* (count scores) 100.0))))


(defn best-grade [course-data student-name]
  (let [scores (:scores (find-student course-data student-name))]
    (reduce max scores)))


(defn worst-grade [course-data student-name]
  (let [scores (:scores (find-student course-data student-name))]
    (reduce min scores)))



;; Example usage
;;
;; (println "Here is how Jill did in the course:")
;; (println "Overall Grade:" (course-grade course-data "Jill"))
;; (println "Best Grade:" (best-grade course-data "Jill"))
;; (println "Worst Grade:" (worst-grade course-data "Jill"))
