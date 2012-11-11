(ns friendly-error.core)

(defn friendly-forms
  [fun & checklists]
  (if (fn? (first checklists))
    (recur fun [checklists])
    `(vary-meta
      (fn
        ~@(map
           (fn [checks]
             (let [anames
                   (map (comp symbol
                              (partial str "arg"))
                        (range (count checks)))
                   achecks
                   (vec (partition
                         2
                         (interleave
                          checks anames)))]
               `([~@anames]
                   {:pre ~achecks}
                   (~fun ~@anames))))
           checklists))
        assoc
        :friend {:fun ~fun
                 :checks (list ~@checklists)})))


(defn friendly-fn
  [fun & checks]
  (eval (apply friendly-forms fun checks)))

(defmacro make-friend!
  [fun & checks]
  `(do
     (alter-var-root (var ~fun)
                     friendly-fn ~@checks)
     (var ~fun)))

(defmacro un-friend! [fun]
  `(when-let [friend# (:friend (meta ~fun))]
     (alter-var-root (var ~fun)
                     (constantly (:fun friend#)))
     friend#))

;;(friendly map is-function? is-collection?)
;;


(defn q1 [a] (inc a))
(defn q2
  ([a]   a)
  ([a b] (+ a b)))



(clojure.core/vary-meta
 (clojure.core/fn
  ([arg0] {:pre [(number? arg0)]} (q2 arg0))
  ([arg0 arg1] {:pre [(number? arg0) (number? arg1)]} (q2 arg0 arg1))
  clojure.core/assoc
  :friend
  {:fun q2, :checks (clojure.core/list [number?] [number? number?])}

