[-> Interpreter](01-interpreter.md)

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

1. [Interpreter](01-interpreter.md)
2. [Some paragraph](#paragraph1)
    1. [Sub paragraph](#subparagraph1)
3. [Another paragraph](#paragraph2)

[-> Interpreter](01-interpreter.md)
