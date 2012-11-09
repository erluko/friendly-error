(ns friendly-error.core)

(defn friendly-forms
  [fun & checks]
  (let [anames (map symbol
                    (map (partial str "arg")
                         (range (count checks))))
        achecks (vec (partition
                 2
                 (interleave checks anames)))]
    `(vary-meta (fn [~@anames]
                  {:pre ~achecks}
                  (~fun ~@anames))
                assoc :friend ~fun)))

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
                     (constantly friend#))))

;;(friendly map is-function? is-collection?)
;;
