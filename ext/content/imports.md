Imports serve a number of purposes in Rapture, though the way it uses them is
controversial. They're used in a familiar way to import functionality, for
example,

```
import rapture.json._
```

and sometimes for more specific DSLs, such as

```
import htmlSyntax._
```

They're also used to specify runtime behavior, such as

```
import patternMatching.exact._
```

and even compile-time configuration, for example by changing the types that
methods return:

```
import modes.returnTry._
```

This, in particular, can cause people some concern.

### Coding standards

Imports, particularly when combined with implicits, have great potential to be
confusing, so teams often enforce various limitations on how and when they
should be used. Often that means that as few types and values as possible
should be imported, and each should be named explicitly in the import
statement, so wildcard imports are often forbidden.

I was faced with the choice between trying to fit Rapture into these common
constraints—the Java style of importing—but instead I decided to go in the
other direction, and fully embrace the power they offer, and one thing you will
notice about Rapture code is that there can be a lot of imports.

In general, when I'm faced with a choice between avoiding a feature because
it's "too difficult", and trying to educate users about that feature through
more exposure, I'll favor the latter, purely in the interests of the long-term
benefits of better understanding.

So, what is this power I'm not willing to give up in exchange for code that's
easier to reason about?

### Scoping

Given that Rapture makes extensive use of configuration imports—those which
change the runtime or compile-time behavior of subsequent code—we can
reasonably ask the question: what are they configuring? The answer is
everything within their lexical scope, and the position of the import lets us
choose that scope.

We can include an import at the top of a file and have it apply to the whole
file, within a class or object body and have it apply just within that body, or
constrain the import to a single method body. Most importantly, we get to
choose.

This encourages a style of programming which is declarative. At the start of a
source file, we can have a number of declarations which serve as a list of
statements about the configuration being applied to that whole file.

### Wildcard imports

In Rapture, all imports are designed (that is to say, named and structured) to
be imported as wildcards. For example, we import
`rapture.time.dateFormats.longEuropean._`, rather than
`rapture.time.dateFormats.longEuropean`, regardless of the fact that we are
only importing a single item, an implicit typeclass instance.

There's a very good reason for this.

In the case of most typeclasses in Rapture, a choice between a number of
alternatives is offered: a choice of ways to format dates; a choice of JSON
backends; a choice of ways to pattern match; a choice of character encodings.
It never makes sense to have two or more implicit instances of the same
typeclass type in scope at the same time: this would lead to an "ambiguous
implicts" error at compile time.

This can be avoided by ensuring that any new such import shadows any previous
imports of the same type, and we do this by giving them the same name, so both
the `rapture.time.dateFormats.longUs` and
`rapture.time.dateFormats.shortEuropean` objects contain implicit `DateFormat`
instances with the name `implicitDateFormat`, and importing either one will
shadow, or "clobber", all previous imports with the same name, and avoid any
ambiguity during implicit resolution.

This works in application code when switching between a number of different
configurations, but is particularly useful in the Scala REPL. Here's an example
with date formats:

```
> import rapture.time._
import rapture.time._

> val date = 20-Mar-2016
date: rapture.time.Date = 20-Mar-2016

> import dateFormats.shortUs._
import dateeFormats.shortUs._

> date.format
res0: String = 03/20/16

> import dateFormats.shortEuropean._
import dateFormats.shortEuropean._

> date.format
res1: String = 20/03/16
```

### Small packages, clear names, conventions

A couple of simple conventions are employed to try to make working with imports
in Rapture easier.

Firstly, packages are kept as small as possible, types and objects which are
intended for internal use are made private, or nested within other objects. As
much as possible, the visible types and values within a top-level package like
`rapture.core` is kept to a minimum.

To mitigate the potential lack of clarity about which imports (and hence which
typeclassees) are affecting the runtime or compile-time behavior of particular
code, Rapture employs the low-tech solution of giving each import a very
clear (and sometimes verbose) name.

Whilst IDEs can offer assistance in debugging the imports that affect the
behavior of different method calls, in the absence of that, we can fall back on
common-sense reasoning: we should be able to make a reasonable guess, for
example, that the `dateFormats.shortEuropean._` import is affecting a call to
`date.format`.

Rapture also employs a common and predictable naming scheme for typeclass
imports. For a given typeclass type, say `Foo`, for which a number of
alternatives exist—let's call them `bar`, `baz` and `quux`—we can expect to
find these options in an object (or package, if it's intended to be open to
later additions) called `foos`: the type name, starting with a lower-case
letter, and pluralized. The actual implicits, within objects called `bar`,
`baz` and `quux`, will all be named `implicitFoo`: the time name, prefixed with
`implicit`.

```
package rapture.example
object foos {
  object bar { implicit val implicitFoo = ... }
  object baz { implicit val implicitFoo = ... }
  object quux { implicit val implicitFoo = ... }
}
```

Hence, to choose use the `baz` typeclass instance, we would `import foos.baz._`.

This import is, of course, relative to the hypothetical `rapture.example`
package. It's a matter of personal style in your own code, but we recommend
putting relative imports on the same line as the import they're relative to,
for example:

```
import rapture.example._, foos.baz._
```

### Package objects

Occasionally, we want an import to be global to our entire package, not just a
single file. Package objects provide a way to share types and implicits
throughout an entire package, but imports are not sufficient for this: their
scope is limited to the body of the package object, but is not re-exported to
the package. Instead we need to create type aliases and duplicate value
definitions. Scala doesn't offer any shortcuts for these aliases, so each one
has to be written out individually.

However, Rapture does, by convention, offer a little help. All single-typeclass
objects, whose contents would normally be imported using a wildcard import,
have an `apply()` method which resolves to that typeclass instance. So an
import such as `import rapture.core.modes.returnTry._` may be included in a
package object as

```
package object myApp {
  import rapture.core._
  implicit def mode = modes.returnTry()
}
```

This, at least, saves a few keystrokes.

### Conclusion

Rapture doesn't shy away from using imports and implicits extensively, simply
for the reason that they offer a well-scoped, declarative programming style,
which gives us much power in writing concise, boilerplate-free code. The usage
may be controversial for its lack of explicitness around which imports impact
which method calls, but as much as possible, simple conventions and clear
naming are used to minimize potential for confusion.

Hopefully the benefits of expressivity outweigh the compromises.
