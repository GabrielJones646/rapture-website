package io.rapture.site

import rapture.web._, httpBackends.jetty._
import rapture.dom._
import rapture.html._
import rapture.codec._
import rapture.uri.{Link => ULink, _}
import rapture.net._
import rapture.io._
import rapture.mime._
import rapture.time._
import rapture.json._, jsonBackends.jawn._

import encodings.`UTF-8`.{name => _, _}

import htmlSyntax._
import dateFormats.longEuropean._

import RequestExtractors._

object `package` {
  // FIXME: This should go into rapture-uri
  def anch(s: String): ULink = new ULink {
    override def toString = s"#$s"
    def absolute = true
  }
  
  def mailto(s: String): ULink = new ULink {
    override def toString = s"mailto:$s"
    def absolute = true
  }

  val currentVersion = "2.0.0-M3"
}

object Main {

  def main(args: Array[String]): Unit = HttpServer.listen(10002) { r => try {
    r match {
      case Path(^) if r.serverName == "scala.world" =>
        Pages.scalaWorld()
      case Path(^) if r.serverName == "propensive.com" =>
        Pages.propensive()
      case Path(^) if r.serverName == "rapture.io" =>
        Pages.home()
      case Path(^ / "css" / f) =>
        implicit val mime = MimeTypes.`text/css`
        uri"classpath:css/$f".input[Char]
      case r@Path(^ / "sample.json") =>
        if(r.param('group, "") == "beatles") json"""{
	  "groups": [
	    {
	      "groupName": "The Beatles",
	      "members": [
		{ "name": "John Lennon", "born": 1940 },
		{ "name": "Paul McCartney", "born": 1942 },
		{ "name": "Ringo Starr", "born": 1940 },
		{ "name": "George Harrison", "born": 1943 }
	      ]
	     }
	  ]
	}""" else json"{}"
      case Path(^ / "download" / "latest") =>
        uri"http://search.maven.org/remotecontent?filepath=com/propensive/rapture_2.11/$currentVersion/rapture_2.11-$currentVersion.jar"
      case Path(^ / "script" / f) =>
        implicit val mime = MimeTypes.`text/javascript`
        uri"classpath:script/$f".input[Char]
      case Path(^ / "content" / f) =>
        implicit val mime = MimeTypes.`text/plain`
        uri"classpath:content/$f".input[Char]
      case Path(^ / "images" / f) =>
        implicit val mime = MimeTypes.`image/jpeg`
        uri"classpath:images/$f".input[Byte]
      case Path(^ / "intro") =>
        Pages.basicPage("Getting Started with Rapture", "intro", List("A brief tour", "Parse the response as JSON", "Extracting the JSON into a Case Class", "Embed the values into some HTML", "Serve it as a web page"))
      case Path(^ / "mod" / mod) =>
        Site.modules.find(_.shortName == mod).map { case Module(name, id, desc, _) =>
          Pages.basicPage(name, s"overview-$id", Nil, showAuthor = false)
        }.getOrElse(Pages.notFound()): HtmlDoc
      case Path(^ / "blog" / "rapture-manifesto") =>
        Pages.basicPage("The Rapture Manifesto", "rapture-manifesto", Nil)
      case Path(^ / "learn") =>
        Pages.basicPage("Tutorials", "learn", Nil, showAuthor = false)
      case Path(^ / "lists") =>
        Pages.basicPage("Mailing Lists", "mailing-lists", Nil, showAuthor = false)
      case Path(^ / "api") =>
        Pages.basicPage("Rapture API", "api-docs", Nil, showAuthor = false)
      case _ =>
        Pages.notFound()
    } } catch {
      case e: Exception => HtmlDoc(Html(Head, Body("Failed")))
    }
  }
}

case class Module(name: String, shortName: String, overview: String, show: Boolean = true)

object Site {

  val intro = Div(id = 'introText)(
    P("Rapture is a family of Scala libraries providing beautiful idiomatic and typesafe Scala APIs for common programming tasks, like working with I/O, cryptography and JSON & XML processing."),
    P("All Rapture libraries are open source, and are available for Scala 2.10 and 2.11 under the Apache Software License 2.0. Source code is on GitHub, and binaries are available on Maven Central.")
  )

  val modules = List(
    Module("JSON", "json", "Rapture JSON makes it easy to integrate dynamically-typed JSON into statically-typed Scala."),
    //Module("I/O", "io", "Rapture I/O provides support for synchronous streaming of data between a variety of heterogeneous resources."),
    Module("Core", "core", "Rapture Core provides some common utilities, including failure handling with Modes."),
    //Module("Crypto", "crypto", "Rapture Crypto provides basic cryptography support in Scala."),
    //Module("Command-line Interface", "cli", "Rapture Command-line Interface makes it easy to interact with Scala programs from the shell"),
    Module("Internationalization", "i18n", "Rapture Internationalization provides typesafe internationalized message support."),
    //Module("XML", "xml", "Rapture XML makes it easy to integrate dynamically-typed XML into statically-typed Scala."),
    Module("HTML", "html", "Rapture HTML provides lightweight syntax for generating HTML.")
    //Module("HTTP", "http", "Rapture HTTP makes it easy to handle HTTP requests.")
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
    BlogEntry("rapture-manifesto", 8-Jan-2016, "The Rapture Manifesto")
  )

  val blogs = blogEntries.map { case be@BlogEntry(id, _, _) => (id -> be) }.toMap

}

object Pages {

  private def versionBox() = Div(id = 'versionBox)(
    Div(id = 'currentVersion)(
      Small("Current", Br, "release"),
      currentVersion
    ),
    Div(id = 'buildChoice)(
      Div(id = 'sbtBtn, classes = Seq("selected"), onclick = "document.getElementById('sbtBtn').className = 'selected'; document.getElementById('mavenBtn').className = ''; document.getElementById('jarBtn').className = ''; document.getElementById('maven').style.display = 'none'; document.getElementById('jar').style.display = 'none'; document.getElementById('sbt').style.display = 'block';")("SBT"),
      Div(id = 'mavenBtn, onclick = "document.getElementById('sbtBtn').className = ''; document.getElementById('mavenBtn').className = 'selected'; document.getElementById('jarBtn').className = ''; document.getElementById('maven').style.display = 'block'; document.getElementById('jar').style.display = 'none'; document.getElementById('sbt').style.display = 'none';")("Maven"),
      Div(id = 'jarBtn, onclick = "document.getElementById('sbtBtn').className = ''; document.getElementById('mavenBtn').className = ''; document.getElementById('jarBtn').className = 'selected'; document.getElementById('maven').style.display = 'none'; document.getElementById('jar').style.display = 'block'; document.getElementById('sbt').style.display = 'none';")("JAR")
    ),
    Div(id = 'buildCode)(
      Div(id = 'sbt)(s""""com.propensive" %% "rapture" % "$currentVersion""""),
      Div(id = 'maven, style = "display:none")(
        "<groupId>com.propensive</groupId>", Br,
        "<artifactId>rapture_2.11</artifactId>", Br,
        s"<version>$currentVersion</version>"
      ),
      Div(id = 'jar, style = "display:none")(s"""http://rapture.io/rapture-$currentVersion.jar""")
    )
  )
 
  def scalaWorld() = Template.globalPage("scalaworld", "Scala World 2016")(
    Div(id = 'circle)(
      P(
        "SCALA", Br,
        "WORLD", Br,
	"2016"
      ),
      P(classes = Seq("small"))(
        "11-13 SEPTEMBER"
      ),
      P(classes = Seq("small"))(
        "RHEGED CENTRE", Br,
        "LAKE DISTRICT, UK"
      )
    ),
    P(id = 'moreDetails)("More details coming soon.")
  )

  def propensive() = Template.globalPage("propensive", "Propensive: Scala Consultancy")(
    Div(id = 'main)(
      Img(id = 'propensiveLogo, src = ^ / "images" / "propensiveLogo.png", alt = "Propensive"),
      P("We provide training and consultancy to help developers worldwide realize their aspirations with ", A(href = uri"http://scala-lang.org")("Scala"), "."),
      P("Get in touch at ", A(href = mailto("info@propensive.com"))("info@propensive.com"), ".")
    )
  )

  def basicPage(heading: String, content: String, links: List[String], showAuthor: Boolean = true): HtmlDoc = Template.page(heading)(
    Div(classes = Seq("container", "shortBanner"))(
      Div(classes = Seq("shadow"))(" "),
      H1(" "),
      Div(classes = Seq("shadow3"))(" ")
    ),
    Div(classes = Seq("container", "fixed"))(
      Div(classes = Seq("column", "one-quarter", "follow"))(
        if(links.length > 0) Div(
          H5("Quick Links"),
          Ul(classes = Seq("bullets"))(
	    links.map { ln => Li(
	      A(href = anch(ln.toLowerCase.replaceAll("[^a-z0-9]", "")))(ln)
	    ) }: _*
          )
        ) else Div(" ")
      )
    ),
    Div(classes = Seq("container", "first"))(
      Div(classes = Seq("column", "one-quarter", "follow"))(
        P(Br)
      ),
      Div(classes = Seq("column", "one-half", "page"))(
        H2(heading),
        Script(typ = "text/javascript")(s"includeMd('$content');"),
        Img(src = ^ / "images" / "mono_balloon_small.png", id = 'end)
      ),
      Div(classes = Seq("column", "one-quarter", "sidebar"))(
        if(showAuthor) Div(
          H5("Author"),
          Img(classes = Seq("author"), src = ^ / "images" / "jon.jpg"),
          P(B("Jon Pretty"), Br, A(href = uri"http://twitter.com/propensive")("@propensive"))
        ) else Div(" "),
	H5("Rapture Blog"),
        Ul(
	  "",
	  Blog.blogEntries.map { case BlogEntry(id, date, title) =>
            Li(A(href = ^ / "blog" / id)(title), Br, Span(classes = Seq("date"))(date.format))
	  }: _*
	)
      )
    )
  )
  def notFound(): HtmlDoc = Template.page("Rapture: Not Found")(
    P("The page you have requested cannot be found.")
  )

  def home() = Template.page("Rapture: Home")(
    Div(classes = Seq("container", "banner"))(
      H1(
        Span(id = 'balloon)(Img(src = ^ / "images" / "logo_color.png")),
        "Rapture"
      ),
      Div(classes = Seq("shadow2"))(" ")
    ),
    Div(classes = Seq("container"))(
      Div(classes = Seq("intro", "one-half", "column"))(
        Site.intro
      ),
      Div(classes = Seq("one-half", "column"))(
        versionBox(),
	H5(classes = Seq("whatNow"))("What now?"),
	Ul(classes = Seq("bullets"))(
          Li(A(href = ^ / "intro")("Getting Started with Rapture")),
	  Li(A(href = uri"https://gitter.im/propensive/rapture", target = '_new)("Join the discussion on Gitter")),
          Li(A(href = uri"https://github.com/propensive/rapture")("Explore the source code"))
	)
      )
    ),
    Div(classes = Seq("container"))(
      H5("About Rapture")
    ),
    Div(classes = Seq("container"))(
      Site.keyPoints.map { case (t, c) => Div(classes = Seq("one-third", "column"))(
        H3(t),
        P(c)
      ) }: _*
    )
  )
}

object Template {

  def page(title: String)(pageContent: DomNode[_ <: ElementType, Html5.Flow, _ <: AttributeType]*): HtmlDoc = globalPage("rapture", title)(
	Div("", pageContent: _*),
	Div(id = 'links)(
	  Div(classes = Seq("container", "glass"))(
	    Div(classes = Seq("one-quarter", "column"))(
	      H5("Modules"),
	      Ul(
		Li(A(href = ^ / "mod" / Site.modules.head.shortName)(Site.modules.head.name)),
		Site.modules.tail.map { m => Li(A(href = ^ / "mod" / m.shortName)(m.name)) }: _*
	      )
	    ),
	    Div(classes = Seq("one-quarter", "column"))(
	      H5("Resources"),
	      Ul(
		Li(A(href = uri"https://github.com/propensive/rapture")("Source code")),
		Li(A(href = uri"http://search.maven.org/#artifactdetails|com.propensive|rapture_2.11|$currentVersion|jar")("Binary downloads")),
		Li(A(href = uri"http://www.apache.org/licenses/LICENSE-2.0")("License"))
	      )
	    ),
	    Div(classes = Seq("one-quarter", "column"))(
	      H5("Help and Support"),
	      Ul(
		Li(A(href = ^ / "learn")("Tutorials")),
		Li(A(href = ^ / "lists")("Mailing lists")),
		Li(A(href = ^ / "api")("API Documentation")),
		Li(A(href = uri"https://gitter.im/propensive/rapture")("Gitter")),
		Li(A(href = uri"https://github.com/propensive/rapture/issues")("Issue Tracker"))
	      )
	    ),
	    Div(classes = Seq("one-quarter", "column"))(
	      H5("Propensive"),
	      Ul(
		//Li(A(href = ^ / "propensive" / "support")("Commercial support")),
		//Li(A(href = ^ / "propensive" / "training")("Training")),
		Li(A(href = uri"http://propensive.com")("Contact"))
		//Li(A(href = ^ / "propensive" / "blog")("Blog"))
	      )
	    )
	  )
	)
  )

  def globalPage(ss: String, title: String)(pageContent: DomNode[_ <: ElementType, Html5.Flow, _ <: AttributeType]*): HtmlDoc = HtmlDoc(
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
        Link(rel = stylesheet, href = ^ / "css" / "global.css"),
        Link(rel = stylesheet, href = ^ / "css" / s"$ss.css"),
        Link(rel = stylesheet, href = uri"https://fonts.googleapis.com/css?family=Ovo"),
        Link(rel = stylesheet, href = uri"https://fonts.googleapis.com/css?family=Fira Mono"),
        Link(rel = stylesheet, href = uri"https://fonts.googleapis.com/css?family=Philosopher"),
        Link(rel = stylesheet, href = uri"https://fonts.googleapis.com/css?family=Josefin Sans:400,300,100"),
        Link(rel = stylesheet, href = uri"https://fonts.googleapis.com/css?family=Alegreya Sans:400,300"),
	Script(typ = "text/javascript", src = ^ / "script" / "showdown.js"),
	Script(typ = "text/javascript", src = ^ / "script" / "rapture.js")
      ),
      Body(
        Div(id = 'nav)(
          P(classes = List("remove-bottom"))(
            A(href = uri"http://propensive.com")(
              Img(classes = Seq("miniLogo"), src = ^ / "images" / "propensive.png"),
              "Propensive"
            ),
            A(href = uri"https://rapture.io/")(
              Img(classes = Seq("miniLogo"), src = ^ / "images" / "mono_balloon_small.png"),
              "Rapture"
            ),
            A(href = uri"https://scala.world/")(
              Img(classes = Seq("miniLogo"), src = ^ / "images" / "sw.png"),
              "Scala World"
            ),
            A(id = 'home, href = ^)(
              Img(classes = Seq("miniLogo"), src = ^ / "images" / "home.png"),
              "Home"
            )
          ),
          Div(classes = Seq("shadow"))(" ")
        ),
        pageContent: _*
      )
    )
  )

}
