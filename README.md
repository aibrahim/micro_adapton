# micro_adapton

Micro Implementation of Incremental Computation in Clojure, 
based on this paper https://arxiv.org/abs/1609.05337

## Usage
In repl
```clojure
=> (use 'micro_adapton.adapton)
=> (def r1 (adapton-ref 10))
=> (def r2 (adapton-ref 8))
=> (def a (let [a-temp (mk-athunk (fn [] (- (adapton-compute r1) (adapton-compute r2))))]
            (adapton-add-dcg-edge! a-temp r1)
            (adapton-add-dcg-edge! a-temp r2)
          a-temp))
=> (adapton-compute a)
=> (adapton-ref-set! r1 20)
=> (adapton-compute a)
```
