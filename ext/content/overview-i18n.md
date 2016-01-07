Rapture I18N is a very simple library for working with typesafe localized
strings in Scala. It is part of the [Rapture](http://rapture.io/) project.

### Features

 - Simple format for writing language-specific string literals
 - Internationalized Strings' types reflect the languages they support
 - Strings are typechecked to ensure that strings for all languages are provided
 - Support for language-dependent string substitution
 - Internationalized strings may be treated as normal strings by specifying a
   default language
 - Convenient trait provided for writing 

## Using Rapture I18N

First of all, import the following:

```scala
import rapture.i18n._
```

You can write an internationalized string by writing `en"Hello"` or
`fr"Bonjour"`. These have types I18nString[En] and I18nString[Fr], representing
English and French strings, respectively.

More importantly, you can define a single value representing an
internationalized string using the alternation operator (`|`), like this:

```scala
val greeting = en"Hello" | fr"Bonjour" | de"Guten Tag"
```

The value `greeting` here represents a string in three different languages, and
has the type `I18nString[En with Fr with De]`. Note that the `with` combinator
represents a type intersection, which is the chosen implementation for
combining multiple language types in a single type parameter.

Given a value such as `greeting`, we can access the string for a language of
our choice, simply by applying it as a type parameter to the identifier, e.g.
`greeting[En]` will produce `"Hello"`, whereas `greeting[De]` will produce
`"Guten Tag"`.

### Subtyping

Types for internationalized strings behave such that a value may overspecify
language strings for a given type, for example

```scala
val msgs: I18nString[En with Fr] = fr"Bonjour" | en"Hello" | de"Guten Tag"
```

but may not underspecify language strings, so this is not valid:

```scala
val msgs: I18nString[En with Fr] = en"Hello" | de"Guten Tag"
```

and will result in a compile error.

```scala
error: type mismatch;
 found   : rapture.i18n.I18nString[rapture.i18n.De]
 required: rapture.i18n.I18nString[rapture.i18n.Fr]
       val msgs: I18nString[En with Fr] = en"Hello" | de"Guten Tag"
                                                      ^
```

This helpfully indicates the missing language.

### Substitution

Internationalized strings may be specified with interpolated substitutions,
which can themselves be either ordinary `String`s or other internationalized
strings. In the latter case, the internationalized string must provide the
language of the internationalized string it is being substituted into, for
example this is valid:

```scala
val greeting = en"Hello" | fr"Bonjour"
en"In England we greet people with '$greeting'."
```

but not this,

```scala
val greeting = en"Hello" | fr"Bonjour"
de"In Deutschland sagen wir '$greeting'"
```

which is another compile error.

### Default languages

It is often useful to pick a single language for use within some scope, and use
internationalized strings as if they are ordinary `String`s. This is achievable
by importing a default language, e.g.

```scala
import languages.es._
```

An internationalized string may then be used in place of an ordinary `String`,
like so:

```scala
import languages.de._
val errorMsg = en"Oh no!" | de"Ach nein!"
throw new Exception(errorMsg)
```

which will throw an exception with the German error message.

Without the import of a default language, this would produce a type error at
compile time. Beware, though, of methods like `+` on `String` which simply call
`toString` on the operand, and consequently do not enforce the typesafe
conversion to the correct language string. These methods should be avoided.

### Standard methods

The `toString` method on `I18nString`s is implemented such that it should
quickly be obvious that the string is an internationalized, rather than an
ordinary, string. For a given internationalized string, `toString` will display
two-letter codes for all the languages it provides, followed by the contents of
the string in English, if available, or in some other language, if not.

For example,

```scala
scala> fr"Bonjour" | en"Hello"
res: I18nString[Fr with En] = fr|en:"Hello"
```

Additionally `hashCode` and `equals` are implemented such that `I18nString`s
can be usefully compared. Internationalized strings are treated as sets, so
reorderings are not significant.

### Using I18N in your own projects

A multilingual application, or even a module within a multilingual application
will usually make a decision about what languages it should support throughout.
This decision can be represented as a type, such as `En with Fr with Es`, and
for convenience, we can give this type an alias.

Then, as the project develops, its choice of supported languages may change, so
the type alias becomes a useful way to change the supported languages globally,
and enforce typechecking to ensure that every internationalized string in scope
provides every language. For example, define

```scala
package object app {
  type AppLangs = En with Fr with Es
}
```

or alternatively,

```scala
package object app {
  type IString = I18nString[En with Fr with Es]
}
```

and then it is never necessary to refer to the application languages (`En`,
`Fr`, `Es`) explicitly, but every string parameterized on `AppLangs` will
nevertheless be typechecked to ensure all languages are provided.

It would then be easy to add an additional language, just by changing the type
alias, and recompiling (which would produce a type error every time the
newly-added language has not been provided.

It is often convenient to store all user messages in a single place, to avoid
littering source code with strings. Using internationalized strings, you may
put all your application messages in a single object, like this:

```scala
object Messages {
  val cart = en"Shopping cart" | fr"Panier"
  def items(n: Int) = if(n == 1) en"$n item" | fr"$n article" else en"$n items" | fr"$n articles"
  // more words and phrases
}
```

Throughout your application internationalized strings may be passed around as a
single value which represents several strings in different languages, however
at some point the application will need to present a single string to an
end-user, in a language determined by some runtime value, such as the string
`"EN"` or `"FR"`, "English (US)" or "Deutsch".

This runtime value may come from a user request, from a session or a
configuration file; this is a design choice for the application, and Rapture
I18N imposes no constraints on recommendations on how it should be provided,
except that the application must, at some point, resolve the runtime value
representing the language to its compile-time type representation, and use this
to extract a standard `java.lang.String` from an `I18nString`.

An example may look like this:

```scala
def resolve(msg: I18nString[AppLangs], lang: String): String = lang match {
  case "EN" => msg[En]
  case "FR" => msg[Fr]
  case "ES" => msg[De]
}
```

This logic, which derives a `String` from the runtime values of an `I18nString`
representing a message and a `String` representing a language, is implemented
just once for the entire application, and corresponds to the small surface area
in which runtime errors may occur; all other logic relating to language strings
has been safely typechecked.



