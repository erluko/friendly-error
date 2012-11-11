(ns friendly-error.core)

(defn friendly-forms
  "Helper function for generating forms for
   friendly-error message macro make-friend!

   Given a function and a list of per-argument
   predicates, returns clojure forms for
   defining a function which checks those
   predicate as preconditions and then calls
   the initial function.

   Example:
   (def mymap map)
   (friendly-forms mymap fn? coll?)
   returns the same result as:
     (fn ([arg0 arg1]
            {:pre [(fn? arg0) (coll? arg1)]}
            (mymap arg0 arg1)))

   If your target function has multiple
   signatures, pass vectors of predicates
   rather than just predicates:

   (defn f1 ([a] a) ([a b] (+ a b)))
   (friendly-forms f1 [number?]
                      [number? number?])

  Note: the forms returned include metadata
  hooks with references to the parameters, so
  that (un-friend! (make-friend! ...)) can
  return the arguments passed to
  friendly-forms."
  [fun & checklists]
  (let [checklists
        (if (vector? (first checklists))
          checklists
          [(vec checklists)])]
    `(vary-meta
      (fn
        ~@(map
           (fn [checks]
             (let [anames
                   (map (comp
                         symbol
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
                 :checks ~(vec checklists)})))

(defn friendly-fn
  "Given arguments like those to
   friendly-forms, returns a function with
   the specified per-argument preconditions."
  [fun & checks]
  (eval (apply friendly-forms fun checks)))

(defmacro make-friend!
  "Replaces the current definition of fun with
   one containing the specified per-argument
   preconditions."
  [fun & checks]
  `(do
     (alter-var-root (var ~fun)
                     friendly-fn ~@checks)
     (var ~fun)))

(defmacro un-friend!
  "Reverses the effect of make-friend! and
   returns a map containing enough information
   to invoke make-friend! again."
  [fun]
  `(when-let [friend# (:friend (meta ~fun))]
     (alter-var-root
      (var ~fun)
      (constantly (:fun friend#)))
     friend#))


