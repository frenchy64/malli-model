[<- Introduction](00-intro.md)

# Interpreter

The most direct way to check a value against a specification is to implement an _interpreter_.

Imagine opposing fingers simultaneously tracing both the specification and the value.

```
|------| |------|
|      | |      |
| spec | | value|
|      | |      |
|------| |------|
```

At each position, you must interpret the specification on your left
and then check the value on your right against it.

For example, if the specification is `[:= 1]`, you must recognize that:
1. this is a singleton schema because it starts with `:=`,
2. it specifies that only `1` is allowed, and
3. we need devise an algorithm to validate the value on the right as `1`.

Now we go to the right and use our algorithm to check the value.

This has a high cognitive load, and computers don't fare much better.

> Exercise 1

Here's the implementation---but don't stress the details.
Can you find the three steps of interpretation for the specification `[:= 1]`?

```clojure
(defn lookup [k R] (or (get R k) (throw (ex-info (str "not in scope: " k) {}))))
(defn interpret [s {:keys [R] :as o} x]
  (if (contains? R s)
    (interpret (lookup s R) o x)
    (case (if (vector? s) (first s))
      := (let [[_ y] s] (= x y))
      :seqable (let [[_ c] s] (and (seqable? x) (every? #(interpret c o %) x)))
      (throw (ex-info (str "invalid schema " (pr-str s)) {})))))
(defn valid? [s o v] (interpret s o v))
```

The line is here:

```clojure
      := (let [[_ y] s] (= x y))
```

The `case` clause `:=` identifies the singleton schema, `y`
will be `1`, and `(= x y)` is the algorithm to validate the value `x`.

We can trace the interpreter and observe its execution.
To do this, run `lein repl` to start a REPL.

Make sure you're in the `malli-model.interpreter` namespace.

```clojure
(valid? [:= 1] {} 1)
; 0 (valid? [:= 1])
; 1 | (interpret [:= 1])
; 1 | => true
; 0 => true

(valid? [:= 1] {} 2)
; 0 (valid? [:= 1])
; 1 | (interpret [:= 1])
; 1 | => false
; 0 => false
```

Recursive values are checked by interpreting the schemas at runtime.

```clojure
(valid? :onion {:R {:onion [:seqable :onion]}} [[[[[[[]]]]]]])
;= 0 (valid? :onion)
;= 1 | (interpret :onion)
;= 2 | | (lookup :onion)
;= 2 | | => [:seqable :onion]
;= 2 | | (interpret [:seqable :onion])
;= 3 | | | (interpret :onion)
;= 4 | | | | (lookup :onion)
;= 4 | | | | => [:seqable :onion]
;= 4 | | | | (interpret [:seqable :onion])
;= 5 | | | | | (interpret :onion)
;= 6 | | | | | | (lookup :onion)
;= 6 | | | | | | => [:seqable :onion]
;= 6 | | | | | | (interpret [:seqable :onion])
;= 7 | | | | | | | (interpret :onion)
;= 8 | | | | | | | | (lookup :onion)
;= 8 | | | | | | | | => [:seqable :onion]
;= 8 | | | | | | | | (interpret [:seqable :onion])
;= 9 | | | | | | | | | (interpret :onion)
;=10 | | | | | | | | | | (lookup :onion)
;=10 | | | | | | | | | | => [:seqable :onion]
;=10 | | | | | | | | | | (interpret [:seqable :onion])
;=11 | | | | | | | | | | | (interpret :onion)
;=12 | | | | | | | | | | | | (lookup :onion)
;=12 | | | | | | | | | | | | => [:seqable :onion]
;=12 | | | | | | | | | | | | (interpret [:seqable :onion])
;=13 | | | | | | | | | | | | | (interpret :onion)
;=14 | | | | | | | | | | | | | | (lookup :onion)
;=14 | | | | | | | | | | | | | | => [:seqable :onion]
;=14 | | | | | | | | | | | | | | (interpret [:seqable :onion])
;=14 | | | | | | | | | | | | | | => true
;=13 | | | | | | | | | | | | | => true
;=12 | | | | | | | | | | | | => true
;=11 | | | | | | | | | | | => true
;=10 | | | | | | | | | | => true
;= 9 | | | | | | | | | => true
;= 8 | | | | | | | | => true
;= 7 | | | | | | | => true
;= 6 | | | | | | => true
;= 5 | | | | | => true
;= 4 | | | | => true
;= 3 | | | => true
;= 2 | | => true
;= 1 | => true
;= 0 => true
true
```

Notice that this implementation spends extra computation time interpretting schemas.
This will be addressed by the next section.

[<- Introduction](00-intro.md)
[Compiler ->](02-compiler.md)
