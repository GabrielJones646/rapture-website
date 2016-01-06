I have been working on Rapture, a collection of Scala libraries, since 2010. It
offers intuitive APIs for a number of everyday programming tasks, while using
some more advanced features of Scala to try to take best advantage of the
powerful typesafety available in the language. It's fun, and it's a great basis
for experimenting with advanced functionality in Scala.

Rapture includes a comprehensive *JSON library*, and an experimental
counterpart for working with *XML*. The project started around development of a
libary for working with *file and network I/O*. Smaller modules exist for
typesafe *internationalization*, *cryptography* and *codecs*, reading *CSV*
files, writing *LaTeX*, and working with *HTML* and *HTTP*. The modules share a
common philosophy of being typesafe, intuitive, extensibile, configurable,
idiomatic and unopinionated. These themes are discussed in more detail on the
[home page](/).

The modules in Rapture share a number of desirable qualities: They provide
useful functionality for Scala which often isn't available elsewhere, and code
written using Rapture is typically clear and beautiful to read. It's DRY, with
close to zero boilerplate. It offers choices in which third-party libraries it
relies upon, so it fits in well with other dependencies. [Simone
Scarduzio](https://twitter.com/codesigner_eu) recently commented on Twitter
that Rapture "could easily be the Guava/Boost of Scala...". Whatever you think
of Guava and Boost, that's high praise.

<blockquote class="twitter-tweet" data-conversation="none" lang="en"><p lang="en" dir="ltr"><a href="https://twitter.com/fanf42">@fanf42</a> <a href="https://twitter.com/propensive">@propensive</a> totally agree, it could easily be the Guava/Boost of Scala, some sort of unofficial stdlib.</p>&mdash; Simone Scarduzio (@codesigner_eu) <a href="https://twitter.com/codesigner_eu/status/679419462890733570">December 22, 2015</a></blockquote>
<script async src="//platform.twitter.com/widgets.js" charset="utf-8"></script>

### "Zero-user Library"

But Rapture doesn't have many users. [Li Haoyi](https://twitter.com/li_haoyi)
called it a "zero-user library", which—although brutal—is an honest
assessment when comparing it to other libraries like Shapeless or Scalaz which
have very active communities around them. There are reasons for this; some have
been fixed now, some are in the process of being fixed; and some are maybe not
yet known.

Past difficulties publishing Rapture to Maven Central, and poor documentation,
probably more than any other reasons, have meant that of all the Scala
libraries out there, Rapture probably gets the least usage for its usability;
the least uptake for the effort that's gone into it.

And this is what I intend to fix in 2016.

But how? The first thing I did was to watch [Erik
Osheim](https://twitter.com/d6)'s [talk at Scala
World](https://www.youtube.com/watch?v=iKyIKozv8a8). Erik has done a fantastic
job of nurturing a community around the Cats project in a short space of time,
and his ninety-minute talk was a masterclass in how to make open-source
software and the communities around it work together effectively, and to make
those projects successful.

I took a lot of valuable points from Erik's talk, and along with my own
observations about Rapture, and its place in the Scala ecosystem, this is my
eight-point manifesto for making Rapture a success.

## The Rapture Manifesto

1. *Access* — It should be easy to depend on Rapture, and use it in your own
   projects.
2. *Website* — Information about Rapture should be available on the
   [rapture.io](http://rapture.io/) website, easy to find, accurate, and up to
   date.
3. *Documentation* — in the form of explanatory blog posts, tutorials and
   reference documentation should be discoverable, easy to contribute to, and
   continually growing
4. *Issues* — The [issue tracker](https://github.com/propensive/rapture/issues)
   should be an accurate reflection of the state of Rapture, including bugs,
   unimplemented features and documentation issues.
5. *Roadmap* — A roadmap for the future direction of Rapture should be published
   on [the website](http://rapture.io/) and maintained.
6. *Testing* — We should aspire to reach 100% test coverage of the Rapture
   codebase.
7. *Quality* — Code quality standards should be established, adopted and
   progressively retrofitted
8. *Performance* — Rapture libraries should be benchmarked, and work done to
   ensure all operations offer reasonable performance

I'll discuss each of the key points on the manifesto in more detail.

### Access

Rapture development started before SBT existed. For many years, Rapture
continued to be built with `make`, and for a project which (at the time) had no
third-party dependencies, there were few compelling reasons to invest time in
migrating to a new, more complicated build system.

However, as SBT's usage grew, and the open-source community standardized on
publishing to Maven Central, people wanted—and expected—to be able to include
Rapture in their own projects just by adding a one-liner to their SBT builds.
And, unfortunately it was never that straightforward. Publishing to Maven
Central without SBT was a painful experience. In my darkest moments of trying,
with the limited documentation available, I even wrote a humorous "create your
own adventure"
[game](http://play.textadventures.co.uk/Play.aspx?id=zv-wer8keey6rnhk4am25qhttp://play.textadventures.co.uk/Play.aspx?id=zv-wer8keey6rnhk4am25q),
the (unachievable) goal of which was to publish a library to Maven Central.

But for a few months now, thanks to the tireless efforts of Alistair Johnson,
Rapture is available on Maven Central, and including it as a dependency is as
easy as for any other Scala library.

### Website

You are reading this blog post on the new [rapture.io](http://rapture.io/)
website, an evolution of its previous incarnation, but designed to be easier to
maintain. It's managed in a public Git repository, and builds with SBT, so
changes should flow more freely than in the past.

It was a conscious decision to launch the site as soon as it reached
"adequacy". Whilst grand plans have long existed for ways to enhance the
website, these shouldn't be blockers on regular, gradual updates and
improvements. It's a work-in-progress, and will remain so for the foreseeable
future.

An novel feature of the website is that it's written using Rapture's HTML and
HTTP modules.

### Documentation

I think the biggest issue for Rapture is its poor documentation, so this shall
be my main focus over the coming months. Most modules are completely
undocumented, and documentation which does exist is difficult to find, and
often incomplete.

This is the first in a series of blog articles about different aspects of
Rapture, and aim to publish one every few days for as long as I still
have material, which I anticipate will fill six months.

A blog article may highlight some interesting aspect of a library, or provide
some background explanation for why certain design choices were made, but it
doesn't provide a great reference material. So in tandem with each blog
article, I will aim to write the reference documentation corresponding to the
APIs or topics covered in that blogpost.  This approach should also help
suggest a strategy for completing the documentation.

To kick things off, I have written a short Getting Started guide, which tours
some of the functionality provided by Rapture's Core, JSON, Net, HTML and HTTP
modules, and should offer a good overview of what Rapture can do. [Read
it](/intro)!

### Issues

As well as improving Rapture's  documentation, to be successful it needs a
supportive community, and as the project owner, it's my job to facilitate and
encourage that community to grow to the critical mass it needs to support
itself.

Bug reports and pull requests to Rapture have always been very welcome, but
were always infrequent, and had a tendency to arrive unexpectedly. And
without good, clear guidance on how to contribute, this is hardly surprising.

A lot of work is involved in maintaining a database of issues, and curating
them with meaningful titles, useful tags (for example "low-hanging fruit"), and
triage. But it benefits everyone as an additional form of documentation, a
potential time-saver for users when something doesn't work as expected, and as
a go-to resource for contributors to find opportunities to get involved in
Rapture's development—something I would love to encourage!

There's no shame in having many open issues, so as long as each is unique and
justified (even if it's something trivial like a documentation error) it's
very welcome in the issue tracker.

### Roadmap

Rapture is still some distance from a final 2.0.0 release. At the back of my
brain there is a list of tasks waiting for an opportunity for completion, and
while some are small features which belong in the issue tracker, others are
complex tasks (for which I may or may not already have at least some part of an
implementation plan) which are significant design choices.

Aspiring contributors can benefit a lot from seeing a good overview of the
direction of the project, so I will begin working on a plan to see Rapture
through to its first "stable" release, and publish that on this site, hopefully
by the end of January. It will be a living document, so we can expect it to
change over time, but it will—for the first time—be more than just internal,
nebulous aspirations.

### Testing

// TODO

### Quality

// TODO

### Performance

The performance of operations in Rapture has always been a secondary concern to
the design of its APIs and their typesafety. That is not to say that it's
unimportant, but that compromise, where there was ever a straight choice
between elegance and performance, were always resolved in favor of the former.

That will remain the case, but much about the performance of Rapture is
unknown, because benchmarking and profiling have never been carried out.

Whilst keeping the focus 

### What else?

And this is where I need help. I have identified some critical issues with Rapture

Why don't you use Rapture? If you tried it, why
did you stop? What could be done to fix that? Join the [Gitter
channel](https://gitter.im/propensive/rapture) and tell me!


