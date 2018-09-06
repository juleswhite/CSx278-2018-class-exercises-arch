(ns arch.sideeffects-test
  (:require [arch.sideeffects :refer :all]
            [clojure.test :refer :all]
            [clojure.java.io :as io]
            [me.raynes.fs :as fs]))


(def dir1 "./test/a")
(def dir2 "./test/b")

(defn create-test-file [d f]
  (let [path (str d "/" f)]
    (io/make-parents path)
    (spit path "a")))

(defn file-names [d]
  (map #(.getName %) (fs/list-dir d)))

(deftest sync-directories-test
  (testing "The correct synchronization of directories"
    (when (fs/exists? dir1)
      (fs/delete-dir dir1))
    (when (fs/exists? dir2)
      (fs/delete-dir dir2))

    (create-test-file dir1 "f1")
    (create-test-file dir2 "f2")

    (sync-directories dir1 dir2)

    (is (= #{"f1" "f2"} (into #{} (file-names dir1))))
    (is (= #{"f1" "f2"} (into #{} (file-names dir2))))    

    (fs/delete-dir dir1)
    (fs/delete-dir dir2)))
    
