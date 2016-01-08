I have been working on Rapture, a collection of Scala libraries, since 2010.
Rapture offers intuitive APIs for a number of everyday programming tasks, while
using some more advanced features of Scala to try to exploit the
powerful typesafety available in the language. It's been fun, and a great
basis for experimenting with some of the more advanced functionality in Scala.

And Rapture is in production use in a number of successful projects.

Rapture includes a comprehensive *[JSON library](/mod/json)*, and an experimental
counterpart for working with *XML*. The project started around development of a
library for working with *file and network I/O*. Smaller modules exist for
typesafe *[internationalization](/mod/i18n)*, *cryptography* and *codecs*,
reading *CSV* files, writing *LaTeX*, and working with *[HTML](/mod/html)* and
*HTTP*. The modules share a common philosophy of being typesafe, intuitive,
extensibile, configurable, idiomatic and unopinionated. These themes are
discussed in more detail on the [home page](/).

The modules in Rapture share a number of desirable qualities: They provide
useful functionality for Scala which often isn't available elsewhere, and code
written using Rapture is typically clear and beautiful to read. It's DRY, with
close to zero boilerplate. It offers choices in which third-party libraries are
used, so fits in well with other dependencies.

One Rapture user, [Simone Scarduzio](https://twitter.com/codesigner_eu)
recently commented on Twitter that Rapture "could easily be the Guava/Boost of
Scala...". Whatever you think of Guava and Boost, that's high praise.

<blockquote class="twitter-tweet" data-conversation="none" lang="en"><p lang="en" dir="ltr"><a href="https://twitter.com/fanf42">@fanf42</a> <a href="https://twitter.com/propensive">@propensive</a> totally agree, it could easily be the Guava/Boost of Scala, some sort of unofficial stdlib.</p>&mdash; Simone Scarduzio (@codesigner_eu) <a href="https://twitter.com/codesigner_eu/status/679419462890733570">December 22, 2015</a></blockquote>
<script async src="//platform.twitter.com/widgets.js" charset="utf-8"></script>

### "Zero-user Library"

But Rapture doesn't have many users. [Li Haoyi](https://twitter.com/li_haoyi)
referred to it as a "zero-user library", which—although brutal—is an honest
assessment when comparing it to other libraries like
[Shapeless](https://github.com/milessabin/shapeless) or
[Scalaz](https://github.com/scalaz/scalaz) which have very active communities
around them. There are reasons for this; some have been fixed now; some are in
the process of being fixed; and some are maybe not yet known.

Past difficulties publishing Rapture to Maven Central, and poor documentation,
probably more than any other reasons, have meant that of all the Scala
libraries out there, Rapture probably gets the least usage for its usability;
the least uptake for the effort that's gone into it.

*This is what I intend to fix in 2016.*

But how? The first thing I did was to watch [Erik
Osheim](https://twitter.com/d6)'s [talk at Scala
World](https://www.youtube.com/watch?v=iKyIKozv8a8). Erik has done a fantastic
job of nurturing a community around the [Cats](https://github.com/non/cats)
project in a short space of time, and his ninety-minute talk was a masterclass
in how to make open-source software and the communities around it work together
effectively; to make those projects successful.

I took many valuable points from Erik's talk, and along with my own
observations about Rapture, and its place in the Scala ecosystem, this is my
eight-point manifesto for making Rapture a success.

## The Rapture Manifesto

1. *Access* — It should be easy to depend on Rapture, and use it in your own
   projects.
2. *Website* — Information about Rapture should be available on the
   [rapture.io](http://rapture.io/) website, easy to find, accurate, and up to
   date.
3. *Documentation* — Explanatory blog posts, tutorials and reference
   documentation should be available, discoverable, easy to contribute to, and
   continually growing.
4. *Issues* — The [issue tracker](https://github.com/propensive/rapture/issues)
   should be an accurate reflection of the state of Rapture, including bugs,
   unimplemented features and documentation issues.
5. *Roadmap* — A roadmap for the future direction of Rapture should be published
   on [the website](http://rapture.io/) and maintained.
6. *Testing* — We should aspire to reach 100% test coverage of the Rapture
   codebase.
7. *Quality* — Code quality standards should be established, adopted and
   progressively retrofitted.
8. *Performance* — Rapture libraries should be benchmarked, and work done to
   ensure all operations perform reasonably well.

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
[game](http://play.textadventures.co.uk/Play.aspx?id=zv-wer8keey6rnhk4am25q),
the (unachievable) goal of which was to publish a hypothetical library to Maven
Central.

But for a few months now, thanks to the tireless efforts of [Alistair
Johnson](https://github.com/InTheNow), Rapture is available on Maven Central,
and including it as a dependency is as trivial as for any other Scala library.

### Website

You are reading this blog post on the new [rapture.io](http://rapture.io/)
website, an evolution of its previous incarnation, but designed primarily to be
easier to maintain. It's managed in a public Git repository, and builds with
SBT, so changes should flow more freely than in the past.

It was a conscious decision to launch the site as soon as it reached
"adequacy". Whilst grand plans have long existed for ways to enhance the
website, these shouldn't be blockers on regular, gradual updates and
improvements. It's a work-in-progress, and will remain so for the foreseeable
future.

A novel feature of the website is that it's written using Rapture's HTML and
HTTP modules. We eat our own dogfood.

### Documentation

The biggest issue for Rapture is almost certainly its poor documentation, so
this shall be my main focus over the coming months. Most modules are completely
undocumented, and documentation which does exist is difficult to find, and
often incomplete.

This is the first in a series of blog articles about different aspects of
Rapture, and I shall aim to publish one every few days for as long as I still
have material, which I anticipate will fill at least half a year.

A blog article may highlight some interesting aspect of a library, or provide
some background explanation for why certain design choices were made, but it
doesn't make great reference material. So in tandem with each blog
article, I will aim to write the reference documentation corresponding to the
APIs or topics covered in that blogpost.  This approach should also help
suggest a strategy for completing the documentation.

To kick things off, I have written a short Getting Started guide, which tours
some of the functionality provided by Rapture's Core, JSON, Net, HTML and HTTP
modules, and should offer a good overview of what Rapture can do. [Read
it](/intro)!

Right now, we have no guarantees that code in documentation works. [Rob
Norris](https://twitter.com/tpolecat)'s [tut](https://github.com/tpolecat/tut)
can fix that, so as soon as I find time, I'll configure it to run on the blogs
and tutorials on this site.

### Issues

As well as improving Rapture's  documentation, to be successful it needs a
supportive community, and as the project owner, it's my job to facilitate and
encourage that community to grow to the critical mass it needs to support
itself.

Bug reports and pull requests to Rapture have always been very welcome, but
in the past were always infrequent, and had a tendency to arrive unexpectedly.
And without good, clear guidance on how to contribute, this is hardly
surprising.

A lot of work is involved in triaging and maintaining a database of issues, and
curating them with meaningful titles and useful tags (for example
`low-hanging-fruit`). But it benefits everyone as an additional form of
documentation, a potential time-saver for users when something doesn't work as
expected, and as a go-to resource for contributors to find opportunities to get
involved in Rapture's development—something I would love to encourage!

There's no shame in having a large number of open issues. So as long as each is
unique and justified (even if it's something trivial like a documentation
error), it's very welcome in the issue tracker.

### Roadmap

Rapture is still some distance from a final 2.0.0 release. At the back of my
mind there is a list of tasks waiting for an opportunity for completion, and
while some are small features which belong in the issue tracker, others are
complex tasks (for which I may or may not already have at least some part of an
implementation plan) which are significant design choices.

Aspiring contributors can benefit a lot from seeing a good overview of the
direction of the project, so I will begin working on a plan to see Rapture
through to its first "stable" release, and publish that on this site, hopefully
by the end of January. It will be a living document, so it can be expected to
change over time, but it will—for the first time—be more than just internal,
nebulous aspirations.

### Testing

Today, Rapture's test coverage is sporadic. Some modules, like Rapture JSON,
have many tests defined, testing the majority of features of the library,
whereas others have none.

The tests which do exist generally serve two purposes: Firstly, they check that
calling methods with certain inputs produce the expected outputs. But the mere
existence of tests, as a set of examples of Rapture code, ensures that those
examples compile. For a library like Rapture, that is especially important,
when subtle changes—like the reordering of implicits—can affect implicit
resolution and inferred types.

The existing tests are written using Rapture's own simple testing framework,
which currently has to be run manually: it's not part of the standard build
process.  This is far from ideal, so it will be a priority to integrate
Rapture's test framework with SBT. Alistair Johnson's
[sbt-catalysts](https://github.com/InTheNow/sbt-catalysts) project is likely to
form the basis of this integration.

Test coverage tooling in Scala has recently become more reliable, so I will
put [scoverage](https://github.com/scoverage) to task on helping Rapture reach
100% coverage. I was initially skeptical of having this as a goal, but was
persuaded by Erik that it's a worthy cause.

### Quality

The code in Rapture has evolved over five years, and as Scala has changed in
that time, so has my style of coding. Older code looks visibly different from
more recent additions. Choices of identifier names; organization of implicits;
expectations from imports; code formatting: these are all aspects which reflect
on the overall quality of the code, act as subtle barriers to potential
contributors. And they currently leave room for improvement.

So I will, over time, develop a set of metrics against which the quality of
Rapture's code can be measured, and track the progress of adhering to those
standards.

### Performance

The performance of various method calls in Rapture has always been a secondary
concern to their design and typesafety. That is not to say that it's
unimportant, but that compromises—where there was ever a straight choice
between elegance and performance—were historically resolved in favor of the
former.

That will remain the case, but much about the performance of Rapture is unknown
because benchmarking and profiling have simply never been carried out.

So, whilst retaining a focus on API elegance, I'll look to improve our
understanding of Rapture's performance characteristics. In many cases, it will
be possible to improve runtime performance without compromising the APIs, and
some particular hotspots may be accelerated with some careful application of
macros. This should be investigated.

### What else?

So, this is where I need some help.

I have identified some critical issues with Rapture which are holding it back
from wider adoption. But I need to put myself into your shoes and understand
what *you* expect to see from Rapture.

Why don't you use Rapture? If you tried it, why did you stop? What could be
done to fix that? If you didn't try it, what stopped you? Please talk to me!
Join the [Gitter channel](https://gitter.im/propensive/rapture) and tell me, or
[message me on Twitter](https://twitter.com/propensive).


