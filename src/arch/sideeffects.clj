(ns arch.sideeffects
  (:require [me.raynes.fs :as fs]
            [clojure.java.io :as io]))


(defn sync-directories 
  "This function synchronizes two directories.

   The function finds the files that are in d1 but not d2
   and copies them to to d2. It also finds the files in d2
   that are not in d1 and copies them to d1.
  "
  [d1 d2]
  (let [a (into #{} (fs/list-dir d1))
        b (into #{} (fs/list-dir d2))
        needed-in-d1 (filter #(not (contains? a %)) b)
        needed-in-d2 (filter #(not (contains? b %)) a)]

    (doseq [f needed-in-d1]
      (fs/copy f (io/as-file (str d1 "/" (.getName f)))))

    (doseq [f needed-in-d2]
      (fs/copy f (io/as-file (str d2 "/" (.getName f)))))))


;; Example Usage:
;;
;; (sync-directories "/Users/Jules/Downloads/class-example-a" 
;;                   "/Users/Jules/Downloads/class-example-b")))))
