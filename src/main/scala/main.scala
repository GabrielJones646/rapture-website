package io.rapture.site

import rapture.web._, httpBackends.jetty._
import rapture.dom._
import rapture.html._
import rapture.codec._
import rapture.uri.{Link => _, _}
import rapture.net._
import rapture.io._
import rapture.mime._
import rapture.time._

import encodings.`UTF-8`.{name => _, _}

import htmlSyntax._

import RequestExtractors._

object `package` {
  
}

object Main {
  def main(args: Array[String]): Unit = HttpServer.listen(10002) { r => try {
    r match {
      case Path(^ / "css" / f) =>
        implicit val mime = MimeTypes.`text/css`
        uri"classpath:css/$f".input[Char]
      case Path(^ / "images" / f) =>
        implicit val mime = MimeTypes.`image/jpeg`
        uri"classpath:images/$f".input[Byte]
      case Path(^) =>
        Pages.home()
      case Path(^ / "blog" / entry) =>
        Blog.blogEntries.find(_.id == entry).map(Blog.blogPage).getOrElse(Pages.notFound): HtmlDoc
      case _ =>
        Template.page("Other page")(P("Hello world"))
    } } catch {
      case e: Exception => HtmlDoc(Html(Head, Body("Failed")))
    }
  }
}

object Site {
  case class Module(name: String, shortName: String, show: Boolean = true)

  val intro = Div(
    P("Rapture is a family of Scala libraries providing beautiful idiomatic Scala APIs for common programming tasks, like working with I/O, cryptography and JSON & XML processing."),
    P("All Rapture libraries are open source, and are available for Scala 2.10 and 2.11 under the Apache Software License 2.0. Source code is on GitHub, and binaries are available on Maven Central.")
  )

  val modules = List(
    Module("JSON", "json"),
    Module("I/O", "io"),
    Module("Core", "core"),
    Module("Crypto", "crypto"),
    Module("Command-line Interface", "cli"),
    Module("Internationalization", "i18n"),
    Module("XML", "xml"),
    Module("HTML", "html"),
    Module("Web", "web")
  )

  val keyPoints = List(
    "Intuitive" -> "Rapture code is meant to be written and read by humans, so the API is designed so that a single line can concisely convey all the information the programmer needs to understand, without any boilerplate.",
    "Configurable" -> "Implicits are used to specify configuration choices, offering the advantages of a declarative programming style, sensible defaults where appropriate, and the flexibility of Scala’s scoping rules if necessary.",
    "Typesafe" -> "The power to implement and refactor code confidently comes from the expressiveness of Scala’s type system. Rapture uses extensive typing to help exploit the guidance and constraints Scala’s type system provides.",
    "Extensible" -> "Rapture is designed around a tiny core, enriched by numerous features added at standard extension points. Because every feature is implemented this way, it is very easy to add your own.",
    "Idiomatic" -> "Rapture strives for a balance between embracing Scala’s established idioms, and pursuing exciting new ways to approach problems. It is never afraid to use advanced Scala features where they are a good fit, but equally content using simple features where they suffice.",
    "Unopinionated" -> "Scala has a dynamic and diverse ecosystem of libraries, often offering many solutions to the same problem. Rapture has no external dependencies, so doesn’t take sides on which libraries you must use, instead using type classes to support multiple alternative implementations."
  )
}

case class BlogEntry(id: String, date: Date, title: String)

object Blog {

  val blogEntries = List(
    BlogEntry("rapture-manifesto", 1-Jan-2016, "The Rapture Manifesto")
  )

  val blogs = blogEntries.map { case be@BlogEntry(id, _, _) => (id -> be) }.toMap

  def blogPage(entry: BlogEntry): HtmlDoc = Template.page(entry.title)(
    Div(classes = Seq("container", "shortBanner"))(
      P(" ")
    ),
    Div(classes = Seq("container"))(
      Div(classes = Seq("column", "two-thirds"))(
        H2(entry.title),
        Script(typ = "text/javascript")(s"includeMd('blog/${entry.id}')")
      ),
      Div(classes = Seq("column", "one-third"))(
        Ul(classes = Seq("sidebar"))(
	  Li(
	    A(href = ^)("Back Home")
	  ),
	  Li(
	    A(href = ^)("Back Home")
	  )
	)
      )
    )
  )

}

object Pages {
  
  private def versionBox() = Div(id = 'versionBox)(
    Div(id = 'currentVersion)(
      Small("Current Release"),
      "2.0.0-M2"
    ),
    Div(id = 'buildChoice)(
      Div(id = 'sbtBtn, classes = Seq("selected"), onclick = "document.getElementById('sbtBtn').className = 'selected'; document.getElementById('mavenBtn').className = ''; document.getElementById('jarBtn').className = ''; document.getElementById('maven').style.display = 'none'; document.getElementById('jar').style.display = 'none'; document.getElementById('sbt').style.display = 'block';")("SBT"),
      Div(id = 'mavenBtn, onclick = "document.getElementById('sbtBtn').className = ''; document.getElementById('mavenBtn').className = 'selected'; document.getElementById('jarBtn').className = ''; document.getElementById('maven').style.display = 'block'; document.getElementById('jar').style.display = 'none'; document.getElementById('sbt').style.display = 'none';")("Maven"),
      Div(id = 'jarBtn, onclick = "document.getElementById('sbtBtn').className = ''; document.getElementById('mavenBtn').className = ''; document.getElementById('jarBtn').className = 'selected'; document.getElementById('maven').style.display = 'none'; document.getElementById('jar').style.display = 'block'; document.getElementById('sbt').style.display = 'none';")("JAR")
    ),
    Div(id = 'buildCode)(
      Div(id = 'sbt)(""""com.propensive" %% "rapture" % "2.0.0-M2""""),
      Div(id = 'maven, style = "display:none")(
        "<groupId>com.propensive</groupId>", Br,
        "<artifactId>rapture_2.11</artifactId>", Br,
        "<version>2.0.0-M2</version>"
      ),
      Div(id = 'jar, style = "display:none")("""http://rapture.io/rapture-2.0.0-M2.jar""")
    )
  )
 
  def notFound: HtmlDoc = Template.page("Rapture: Not Found")(
    P("The page you have requested cannot be found.")
  )

  def home() = Template.page("Rapture: Home")(
    Div(classes = Seq("container", "banner"))(
      H1(
        Img(src = ^ / "images" / "mono_balloon_small.png", classes = Seq("balloon")),
        "Rapture"
      )
    ),
    Div(classes = Seq("container"))(
      Div(classes = Seq("intro", "one-half", "column"))(
        Site.intro
      ),
      Div(classes = Seq("one-half", "column"))(
        versionBox()
      )
    ),
    Div(classes = Seq("container"))(
      Site.keyPoints.map { case (t, c) => Div(classes = Seq("one-third", "column"))(
        H4(t),
        P(c)
      ) }: _*
    )
  )
}

object Template {

  def page(title: String)(pageContent: DomNode[_ <: ElementType, Html5.Flow, _ <: AttributeType]*): HtmlDoc = HtmlDoc(
    Html(
      Head(
        Meta(charset = encodings.`UTF-8`()),
        Title(title),
        Meta(name = 'description, content = ""),
        Meta(name = 'author, content = ""),
        Meta(name = 'viewport, content = "width=device-width,initial-scale=1,maximum-scale=1"),
        Link(rel = stylesheet, href = ^ / "css" / "base.css"),
        Link(rel = stylesheet, href = ^ / "css" / "skeleton.css"),
        Link(rel = stylesheet, href = ^ / "css" / "layout.css"),
        Link(rel = stylesheet, href = ^ / "css" / "rapture.css"),
        Link(rel = stylesheet, href = uri"http://fonts.googleapis.com/css?family=Ovo"),
        Link(rel = stylesheet, href = uri"http://fonts.googleapis.com/css?family=Fira Mono"),
        Link(rel = stylesheet, href = uri"http://fonts.googleapis.com/css?family=Philosopher"),
	Script(typ = "text/javascript", src = uri"http://cdn.rawgit.com/showdownjs/showdown/1.2.2/dist/showdown.min.js"),
	Script(typ = "text/javascript", src = ^ / "script" / "rapture.js")
      ),
      Body(
        Div(id = 'nav)(
          P(classes = List("remove-bottom"))(
            A(href = ^)("rapture.io")
          )
        ),
        Div(classes = Seq("shadow"))(" "),
        Div("", pageContent: _*),
        Div(id = 'links)(
          Div(classes = Seq("container", "glass"))(
            Div(classes = Seq("one-quarter", "column"))(
              H4("Modules"),
              Ul(
                Li(A(href = ^ / "mod" / Site.modules.head.shortName)(Site.modules.head.name)),
                Site.modules.tail.map { m => Li(A(href = ^ / "mod" / m.shortName)(m.name)) }: _*
              )
            ),
            Div(classes = Seq("one-quarter", "column"))(
              H4("Resources"),
              Ul(
                Li(A(href = uri"https://github.com/propensive/rapture")("Source code")),
                Li(A(href = uri"http://search.maven.org/#artifactdetails|com.propensive|rapture_2.11|2.0.0-M1|jar")("Binary downloads")),
                Li(A(href = uri"http://www.apache.org/licenses/LICENSE-2.0")("License"))
              )
            ),
            Div(classes = Seq("one-quarter", "column"))(
              H4("Help and Support"),
              Ul(
                Li(A(href = ^ / "help" / "tutorials")("Tutorials")),
                Li(A(href = ^ / "help" / "lists")("Mailing lists")),
                Li(A(href = ^ / "help" / "apidocs")("API Documentation")),
                Li(A(href = ^ / "help" / "gitter")("Gitter")),
                Li(A(href = ^ / "help" / "issues")("Issues"))
              )
            ),
            Div(classes = Seq("one-quarter", "column"))(
              H4("Propensive"),
              Ul(
                Li(A(href = ^ / "propensive" / "support")("Commercial support")),
                Li(A(href = ^ / "propensive" / "training")("Training")),
                Li(A(href = ^ / "propensive" / "contact")("Contact")),
                Li(A(href = ^ / "propensive" / "blog")("Blog"))
              )
            )
          )
        )
      )
    )
  )

}
