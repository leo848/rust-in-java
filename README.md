# rust-in-java

This repository attempts to make the best features of Rust
available in Java. It does not depend on any external libraries.

The most important two types of this repo are the `Option<T>` and `Result<T, E>` classes as well as the `Iterator<T>`
interface. They behave - except for some caveats - like their Rust counterparts.

## Iterators

*See the [Rust Docs](https://doc.rust-lang.org/std/iter/index.html) for details (only differences are documented here)*

Since interfaces, unlike traits in Rust, cannot be implemented on foreign types,
`IntoIter<T>` is largely useless. You can, however, create an iterator from a data structure using
`Iterators::from` or any other static method, like `of(...T)`, `range(int, int)`, `empty()`
and many more.  
The traits from Rust (e.g. `FusedIterator`, `DoubleEndedIterator`, `ExactSizeIterator`) are all implemented via Java
interfaces.  
In addition to the standard library iterators and adapters, a large subset of the `itertools` crate
is also included in this repo. If something you want to see is missing, just submit a PR.

## Option<T>

*See the [Rust Docs](https://doc.rust-lang.org/std/option/index.html) for details (only differences are documented
here)*

Because Java does not have proper sum types, an `Option<T>`
just stores a nullable value `T` so that `T` represents `Some(T)` and `null` represents `None`.  
Instantiating works similar to Rust, however for type inference to work, `None` is a static method and must be called
as `None()` (assuming `import static leo.rustjava.Option.*`).  
`Option::unwrap` throws a `NullPointerException` if the option is `None`, this can be checked with `Option::isNone` and
`Option::isNone` respectively.  
Because Java lacks a match statement, it is quite tedious to always call `isNone` and
`isSome` and the monadic operations should be used whenever possible.

## Result<T, E>

*See the [Rust Docs](https://doc.rust-lang.org/std/result/index.html) for details (only differences are documented
here)*

Did I already mention that Java doesn't have sum types? Oh well.   
The implementation option chosen in this repo is to first implement an `Either<L, R>` type, storing
an `Option<L>` and an `Option<R>` (see the [either](https://docs.rs/either/latest/either/enum.Either.html) docs for
the provided methods and stuff). A `Result<T, E>` is then a specialized `Either<T, E>` with many redirections
(e.g. the `isOkAnd` method calls `Either::isLeftAnd`). Except being able to match them, results should feel pretty much
the same as in Rust.

## ControlFlow<B, C> and Try<Output, Residual>

I tried to implement this, but failed. Please help me, Rust and Java gods.

## Completeness

This projects does not promise completeness or correctness, and is subject to change. ***Use at your own risk.***

## Copyright + Licensing

Copyright 2022 Leo Blume

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.

