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

### A brief tour

The following is a brief tour of some of the basic features of a few Rapture
modules. We will fetch some content over HTTP, parse it as JSON and construct a
new case class from it, embed it in an HTML document, and then start an HTTP
server and serve our HTML page. Finally, we will make the whole process more
resilient to errors.

#### Send an HTTP request

```
import rapture.uri._
import rapture.io._
import rapture.net._
import rapture.codec._, encodings.`UTF-8`._
```

We start by creating a URL object to the HTTP endpoint we want to post to.

```
val url: HttpUrl = uri"http://rapture.io/sample.json"
```

Then we send the post request, like so:

```
val response = url.httpPost(Map(
  'group -> "beatles"
))
```

The `httpPost` method accepts a number of different types (and you can define
your own using typeclasses), but if we pass it a `Map[Symbol, String]`, this
will be sent as url/form-encoded content to the endpoint.

We get an `HttpResponse` type back. We would like to read the entire contents
of this response into a `String`, which we can do using the `slurp` method,
like so:

```
val content = response.slurp[Char]
```

Note how the `slurp` method takes a type parameter — `Char`. This determines
that the units of data being slurped are characters, and therefore the output
type should be a `String`. It's also possible to specify `Byte` as the type
parameter, but the return type would then be the `Bytes` type, not a `String`.

If these steps were successful, we should now have a `String` with the content
downloaded from the Internet. It should look like this:

```
{"groups":[{"members":[{"name":"John Lennon","born":1940},{"name":"Paul McCartney","born":1942},{"name":"Ringo Starr","born":1940},{"name":"George Harrison","born":1943}],"groupName":"The Beatles"}]}
```

#### Parse the response as JSON

Now we have a `String` containing some JSON content, we should try to parse it
into structured data. First, let's import the Rapture JSON module, and a
backend. I've chosen [Jawn] here, but Rapture also works with Circe, Play,
Jackson, Argonaut, JSON4S, Lift and Spray, adn the behavior should be largely
identical in all cases.

```
import rapture.json._, jsonBackends.jawn._
```

Parsing the `String` is as simple as calling:

```
val json = Json.parse(content)
```

If the parsing is successful, this will provide us with a value of type `Json`,
which represents valid, parsed, structured JSON data. To check this, we can
access elements from the data structure like so:

```
> val drummer: Json = json.groups(0).members(2).name
drummer: Json = "Ringo Starr"
```

Accessing object values and indexing array elements will always yield another
value of type `Json`. Furthermore, these accesses are guaranteed never to fail
at runtime — it won't ever throw an exception — though if the JSON value you
try to access doesn't exist, the resultant `Json` value will exist, but will be
invalid.

This is a deliberate design choice, and is not a problem for as long as you
remain in the dynamic JSON world. But it will (quite rightly) become a problem
when you try to extract a statically-typed Scala value from the `Json` value,
but such extractions offer a variety of ways of handling failure cases, as you
will see later.

#### Extracting the JSON into a case class

Rather than working with `Json` values, which are effectively untyped, we would
like to work with static Scala types. We can call the `as` method with a Scala
type on any `Json` value to attempt to extract an instance of that type from
the JSON. This works for Scala's primitive types, collection types like this:

```
> json.groups(0).members(0).born.as[Int]
res: Int = 1940
```

We can even extract `Json` values, usually as part of another type. For example,

```
> json.groups(0).members.as[Vector[Json]]
res: Vector[Json] = Vector({"name":"John Lennon","born":1940}, {"name":"Paul McCartney","born":1942}, {"name":"Ringo Starr","born":1940}, {"name":"George Harrison","born":1943})
```

This gives us a `Vector` containing four `Json` fragments extracted from the
original. `Json` values in Rapture are immutable, so the the original value —
`json` — remains unchanged.

Rapture also allows extraction into case classes, provided every parameter of
that case class has a type that itself can be extracted. If we define the case
class, `Member`, like so:

```
case class Member(name: String, born: Int)
```

then extracting a member is as simple as
`json.groups(0).members(0).as[Member]`. But we could additionally define a case
class for a group, like so:

```
case class Group(groupName: String, members: List[Member])
```

and then extracting a complete structure from the JSON source is as simple as:

```
> val groups = json.groups.as[List[Group]]
groups: List[Group] = List(Group(The Beatles,List(Member(John Lennon,1940), Member(Paul McCartney,1942), Member(Ringo Starr,1940), Member(George Harrison,1943))))
```

This makes it very easy to define a statically-typed data structure and extract
a complete JSON tree into it in just a few of lines of code.


#### Embed the values in HTML

Now we have our static data structure, we would like to put it into an HTML
page, using Rapture's HTML module.

```
import rapture.html._, htmlSyntax._
```

We would like a very simple page which looks like this:

```
<html>
  <head>
    <title>The Beatles</title>
  </head>
  <body>
    <h1>The Beatles</h1>
    <ul id="members">
      <li>John Lennon, born 1940</li>
      <li>Paul McCartney, born 1942</li>
      <li>Ringo Starr, born 1940</li>
      <li>George Harrison, born  1943</li>
    </ul>
  </body>
</html>
```

Using the Rapture HTML DSL, we can write that as;

```
val html = Html(
  Head(
    Title("The Beatles")
  ),
  Body(
    H1("The Beatles"),
    Ul(id = 'members)(
      Li("John Lennon, born 1940"),
      Li("Paul McCartney, born 1942"),
      Li("Ringo Starr, born 1940"),
      Li("George Harrison, born  1943")
    )
  )
)

```

But we would like this page to depend on the `Group`, so let's redefine it as a method:

```
def html(group: Group) = Html(
  Head(Title(group.groupName)),
  Body(
    H1(group.groupName),
    Ul(id = 'members)(group.members.map { case Member(n, b) =>
      Li(s"$n, born $b")
    }: _*)
  )
)
```


#### Serve it as a web page

Rapture also includes a very simple interface for serving web pages over HTTP.
This API is likely to be the focus of several improvements in typesafety over
the coming months, but for now we can use it to serve our web page. The API
supports different backend implementations, though currently Jetty is the only
implementation available.

```
import rapture.http._, httpBackends.jetty._
```

We can initialize a new HTTP server, listening on a TCP port, which dispatches
requests to a handler we specify. The handler is a partial function which
matches incoming requests, and returns content.

A number of composable extractors for matching different aspects of the request
are provided in the `RequestExtractors` object,

```
import RequestExtractors._

```

but for now, we will just match on the path of the incoming request, for example,

```
{
  case Path(^ / "beatles") =>
    HtmlDoc(html)
}
```

will match all incoming requests at the path `/beatles/`, and will serve our
HTML content as a page. Wrapping the response in `HtmlDoc` gives it the HTML5
Doctype Declaration header, and Rapture is able to know from this type what
MIME type to use to serve the content.

We can start the server running (synchronously) by telling it to `listen`, like so:

```
HttpServer.listen(8080) {
  case Path(^ / "beatles") =>
    HtmlDoc(html(groups(0)))
}
```

Navigating to `http://localhost:8080/beatles/` in your web browser should now show you a page listing the members of The Beatles, and when they were born.

