(ns arch.sideeffects
  (:require [me.raynes.fs :as fs]
            [clojure.java.io :as io]))


(defn in-dir [d f]
  (io/as-file (str d "/" (.getName f))))

(defn sync-instructions
  "This function synchronizes two directories.

   The function finds the files that are in d1 but not d2
   and copies them to to d2. It also finds the files in d2
   that are not in d1 and copies them to d1.
  "
  [a b d1 d2]
  (let [needed-in-d1 (filter #(not (contains? a %)) b)
        needed-in-d2 (filter #(not (contains? b %)) a)]
    (concat 
      (map #(hash-map :src % :dst (in-dir d1 %)) needed-in-d1)
      (map #(hash-map :src % :dst (in-dir d2 %)) needed-in-d2))))


(defn do-copies [copy-instructions]
  (doseq [copy copy-instructions]
    (fs/copy (:src copy) (:dst copy))))

(defn sync-directories 
  "This function synchronizes two directories.

   The function finds the files that are in d1 but not d2
   and copies them to to d2. It also finds the files in d2
   that are not in d1 and copies them to d1.
  "
  [d1 d2]
  (let [a                 (into #{} (fs/list-dir d1))
        b                 (into #{} (fs/list-dir d2))
        copy-instructions (sync-instructions a b d1 d2)]
    (do-copies copy-instructions)))

;; Example Usage:
;;
;; (sync-directories "/Users/Jules/Downloads/class-example-a" 
;;                   "/Users/Jules/Downloads/class-example-b")
