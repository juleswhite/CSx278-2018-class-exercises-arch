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

   The move-to-a function takes a file path from d1 and
   converts it to a file path in d2. The move-to-b function
   does the opposite. Different file path or network path
   representations can be plugged in by simply changing
   the implementation of what the move-to-* functions do.
  "
  [a b move-to-a move-to-b]
  (let [needed-in-d1 (filter #(not (contains? a %)) b)
        needed-in-d2 (filter #(not (contains? b %)) a)]
    (concat 
      (map #(hash-map :src % :dst (move-to-a %)) needed-in-d1)
      (map #(hash-map :src % :dst (move-to-b %)) needed-in-d2))))


(defn do-copies [copy-instructions]
  (doseq [copy copy-instructions]
    (fs/copy (:src copy) (:dst copy))))


;; We could create a new implementation of do-copies, fs/list-dir, 
;; and move-to-* that would work across a network or use some other
;; I/O primitives beyond files.
(defn sync-directories 
  "This function synchronizes two directories.

   The function finds the files that are in d1 but not d2
   and copies them to to d2. It also finds the files in d2
   that are not in d1 and copies them to d1.
  "
  [d1 d2]
  (let [a                 (into #{} (fs/list-dir d1))
        b                 (into #{} (fs/list-dir d2))
        move-to-a         #(in-dir d1 %)
        move-to-b         #(in-dir d2 %)
        copy-instructions (sync-instructions a b move-to-a move-to-b)]
    (do-copies copy-instructions)))

;; Example Usage:
;;
;; (sync-directories "/Users/Jules/Downloads/class-example-a" 
;;                   "/Users/Jules/Downloads/class-example-b")
