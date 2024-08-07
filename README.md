# malli-model

Small models to study compilation and interpretation techniques of malli schemas.

[Walkthrough](doc/00-intro.md)

## Usage

```clojure
$ lein repl
malli-model.interpreter=> (valid? [:= 1] {} 1)
 0 (valid? [:= 1])
 1 | (interpret [:= 1])
 1 | => true
 0 => true
true
malli-model.interpreter=> (valid? [:= 1] {} 2)
 0 (valid? [:= 1])
 1 | (interpret [:= 1])
 1 | => false
 0 => false
false
malli-model.interpreter=> (valid? :onion {:R {:onion [:seqable :onion]}} [[[nil]]])
 0 (valid? :onion)
 1 | (interpret :onion)
 2 | | (lookup :onion)
 2 | | => [:seqable :onion]
 2 | | (interpret [:seqable :onion])
 3 | | | (interpret :onion)
 4 | | | | (lookup :onion)
 4 | | | | => [:seqable :onion]
 4 | | | | (interpret [:seqable :onion])
 5 | | | | | (interpret :onion)
 6 | | | | | | (lookup :onion)
 6 | | | | | | => [:seqable :onion]
 6 | | | | | | (interpret [:seqable :onion])
 7 | | | | | | | (interpret :onion)
 8 | | | | | | | | (lookup :onion)
 8 | | | | | | | | => [:seqable :onion]
 8 | | | | | | | | (interpret [:seqable :onion])
 8 | | | | | | | | => true
 7 | | | | | | | => true
 6 | | | | | | => true
 5 | | | | | => true
 4 | | | | => true
 3 | | | => true
 2 | | => true
 1 | => true
 0 => true
true
```


## License

Copyright © 2024 Ambrose Bonnaire-Sergeant

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.

### tools.trace

Contains code from tools.trace.

```
Copyright (c) Stuart Sierra, 2008. All rights reserved. The use
and distribution terms for this software are covered by the Eclipse
Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
which can be found in the file epl-v10.html at the root of this
distribution. By using this software in any fashion, you are
agreeing to be bound by the terms of this license. You must not
remove this notice, or any other, from this software.
```
