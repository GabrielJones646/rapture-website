# Getting Started with Rapture

Rapture is designed to be very easy to use out of the box. It does, however,
introduce a number of its own idioms, and a little familiarity with Rapture's
approach will go a long way to making the learning process easy.

The first thing to note is that Rapture uses imports extensively to bring
functionality and configuration into scope, and it's usually necessary to
include a few imports at the start of your program or REPL session before you
can use any of Rapture's functionality. This encourages a declarative style of
coding, that avoids repeating the same configuration options 

Each Rapture module has its own package, so first of all import the contents of
the package you want to use, e.g.

```
import rapture.json._
```

or

```
import rapture.html._
```

In the case of Rapture JSON, a backend is also required to determine how JSON
source is parsed, and how JSON abstract syntax trees are stored in memory, so
we also import, for example:

```
import rapture.json.jsonBackends.jawn._
```

Conventionally, we write this as a relative import on the same line:

```
import rapture.json._, jsonBackends.jawn._
```

Note also that all imports in Rapture are written as wildcard imports. There is
a good reason for this, but for now it's not too important.

It's always useful to be aware of which imports are in scope.

# A brief tour

The following is a brief tour of some of the basic features of a few Rapture modules. We will fetch some content over HTTP, parse it as JSON and construct a new case class from it, embed it in an HTML document, and then start an HTTP server and serve our HTML page. Finally, we will make the whole process more resilient to errors.

## Send an HTTP request

```
import rapture.uri._
import rapture.io._
import rapture.net._
import rapture.codec._, encodings.`UTF-8`
```

We start by creating a URL object to the HTTP endpoint we want to post to.

```
val url: HttpUrl = uri"http://rapture.io/sample/"
```

Then we send the post request, like so:

```
val response = url.httpPost(Map(
  'foo -> "bar"
))
```

The `httpPost` method accepts a number of different types (and you can define
your own using typeclasses), but if we pass it a `Map[Symbol, String]`, this
will be sent as url/form-encoded content to the endpoint.

We get an `HttpResponse` type back. We would like to read the entire contents of this response into a `String`, which we can do using the `slurp` method, like so:

```
val content = response.slurp[Char]
```

Note how the `slurp` method takes a type parameter — `Char`. This determines that the units of data being slurped are characters, and therefore the output type should be a `String`. It's also possible to specify `Byte` as the type parameter, but the return type would then be the `Bytes` type, not a `String`.

If these steps were successful, we should now have a `String` with the content downloaded from the Internet. It should look like this:

```
{
  "foo": "bar"
}
```

## Parse the response as JSON

Now we have a `String` containing some JSON content, we should try to parse it into structured data. First, let's import the Rapture JSON module, and a backend. I've chosen [Jawn] here, but Rapture also works with Circe, Play, Jackson, Argonaut, JSON4S, Lift and Spray, adn the behavior should be largely identical in all cases.

```
import rapture.json._, jsonBackends.jawn._
```

Parsing the `String` is as simple as calling:

```
val json = Json.parse(content)
```



3. Extract the JSON into a case class

4. Serialize the JSON

5. Embed the values into some HTML

6. Serve it as a web page

7. Serve language-dependent content

8. Improve excepetion handling

