(ns friendly-error.core)


(defn friendly
  [fun & checks]
  (prn [fun checks])
  (let [anames (map symbol
                    (map (partial str "arg")
                         (range (count checks))))
        achecks (vec (partition
                 2
                 (interleave checks anames)))]
  (eval `(fn [~@anames]
           {:pre ~achecks}
           (~fun ~@anames)))))

(defmacro make-friend
  [fun & checks]
  `(do
     (alter-var-root (var ~fun)
                     friendly ~@checks)
     (var ~fun)))

;;(friendly map is-function? is-collection?)
;;
