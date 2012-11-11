# friendly-error

A Clojure library designed to assist in the clojure friendly errors
project by Elena Machkasova at the University of Minnesota, Morris.

This is not production-ready code, but rather a starting point for
Elena and her students. This library still uses the old foo.core
naming style, for no good reason.

The intent is to be able to replace any existing function with a
version that applies preconditions to each of the parameters so that
the libraries developed for teaching Clojure at UMN Morris will be
able to trap the exceptions and return friendlier error messages to
students.

## Usage

    > (use 'friendly-errors.core)
    > (def mymap map)
	> (mymap 3 [1 2 3])
	java.lang.Long cannot be cast to clojure.lang.IFn ...
    > (make-friend! mymap fn? coll?)
    > (mymap inc [1 2 3])
    (2 3 4)
	> (mymap 'not-a-fn [1 2 3])
	Assert failed: ...
    > (un-friend! mymap) ; restores mymap

## Todos

 1. Fix repl interaction: For some reason this doesn't work well for
    replacing clojure.core/map. It makes the repl behave
    badly. Un-friending restores the repl without restarting.
 2. Stop using friendly-error.core namespace

## License

Copyright © 2012 Eric Kobrin

Distributed under the Eclipse Public License, the same as Clojure.
