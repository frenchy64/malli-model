# Validators

We're going to study different ways of creating predicates ("validators")
from specifications that resemble Malli syntax.

Today, we're going to start with three schemas and one public function.

```
S ::= [:= v]          -- singleton schema
   |  [:seqable S]    -- seqable schema
   |  K               -- reference schema
R ::= {K S}           -- registry

(valid? S {:keys [R] :as opts} x) : Bool
```

`valid?` is a function that takes:
- a schema `S`,
- an options map containing a _registry_ `R` of keywords to schemas
  that we can _reference_ by name, and
- a value `x` to validate against the schema.

Note: We going to use a convention throughout where the schema `S` is always the first
argument. This will help us study program execution traces.

Note: The words specification and schema will be mostly synonymous, but I like to think
that we're divising a language of schemas to create specifications.
Schemas will be the plural of schema instead of schemata.

## Interpreter

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
and then check that the value on your right against it.

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

We don't need to stress over the implementation in isolation because
we can _use_ the interpreter and observe its execution.

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



## Interpreter
