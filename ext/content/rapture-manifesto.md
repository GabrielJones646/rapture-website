I have been working on Rapture, a collection of Scala libraries, for the last
few years. It offers intuitive APIs for a number of everyday programming tasks,
while using some advanced features of Scala to try to take best advantage of
the powerful typesafety available in the language.

Rapture includes a comprehensive *JSON library*, and an experimental counterpart
for working with *XML*. The project started around development of a libary for
working with *file and network I/O*. Smaller modules exist for typesafe
*internationalization*, *cryptography* and *codecs*, reading *CSV* files, writing
*LaTeX*, and working with *HTML* and *HTTP*. The modules share a common
philosophy of being typesafe, intuitive, extensibile, configurable, idiomatic
and unopinionated. These themes are discussed in more detail on the [home
page](/).

The modules in Rapture share a number of desirable qualities: They provide
useful functionality for Scala which often isn't available elsewhere, and code
written using Rapture is typically clear and beautiful to read. It's DRY, with
close to zero boilerplate. It offers choices in which third-party libraries it
relies upon, so it fits in well with other dependencies. [Simone
Scarduzio](https://twitter.com/codesigner_eu) recently commented on Twitter
that Rapture "could easily be the Guava/Boost of Scala...". That's high praise.

<blockquote class="twitter-tweet" data-conversation="none" lang="en"><p lang="en" dir="ltr"><a href="https://twitter.com/fanf42">@fanf42</a> <a href="https://twitter.com/propensive">@propensive</a> totally agree, it could easily be the Guava/Boost of Scala, some sort of unofficial stdlib.</p>&mdash; Simone Scarduzio (@codesigner_eu) <a href="https://twitter.com/codesigner_eu/status/679419462890733570">December 22, 2015</a></blockquote>
<script async src="//platform.twitter.com/widgets.js" charset="utf-8"></script>

## Zero-user Library

But Rapture doesn't have many users. Li Haoyi called it a "zero-user library",
which—although a brutal—is an honest assessment when compared to
other libraries like Shapeless or Scalaz. There are reasons for this; some have
been fixed now, some are in the process of being fixed. Some are maybe not yet
known.

For years Rapture had an unconventional (non-SBT) build process and was not
available on Maven Central (but, thanks to the efforts of Alistair Johnson,
that's no longer the case), and apart from my regular presentations about
Rapture at conferences and meetup groups—which are very popular with the
audiences—the documentation is woefully lacking, so people find it very hard to
get started.

These two issues, probably more than any other reasons, mean that of all the
Scala libraries out there, Rapture probably gets the least usage for its
usability; the least uptake for the effort that's gone into it.

And this is what I intend to fix.

But how? My first step was to watch Erik Osheim's talk at Scala World. Erik has
done a fantastic job of nurturing a community around the Cats project in a
short space of time, and his ninety-minute talk was a masterclass in how to
build effective open-source communities, and to make those projects successful.

## Documentation

The biggest issue for Rapture (now that it is easy to include in your own
projects) is its poor documentation, so this shall be my main focus over the
coming months. Most modules are completely undocumented, and documentation
which does exist is difficult to find, and often incomplete.

I'm going to devote some time to writing a series of blog articles about
different aspects of Rapture, and aim to publish one every couple of days for
as long as I still have material. A blog article can highlight some interesting
aspect of a library, but doesn't provide a great reference resource. So in
tandem with each blog article, I will write the reference documentation
corresponding to the APIs covered in each blogpost. This will also help suggest
the strategy for completing the documentation.

Writing blog articles is time-consuming, and the frequency of posts will be
limited by my availability. It's a new year, so ambition is high, and I'll be
aiming to publish three or four a week, until I've run out of things to talk
about. We shall, of course, see how this works out.

To kick things off, I have written a short Getting Started guide, which tours
some of the functionality provided by Rapture's Core, JSON, Net, HTTP, HTML and
Internationalization modules, and should offer a good overview of what Rapture
can do. Read it!

## Community

As well as improving Rapture's  documentation, to be successful it needs a
supportive community, and as the project owner, it's my job to facilitate and
encourage that community to grow to the critical mass it needs to support
itself.

Bug reports and pull requests to Rapture have always been very welcome, but
were always infrequent, and had a tendency to arrive by unexpectedly. And
without good, clear guidance on how to contribute, this is hardly surprising.
Aspiring contributors can benefit a lot from seeing a good overview of the
direction of the project, 

Rapture is still some distance from completion. At the back of my brain there
is a list of tasks waiting for an opportunity for completion. Many of these are
complex tasks (for which I may or may not already have at least some part of an
implementation plan), while others are simpler loose ends which are just
waiting to be tied up; often what is lacking is a motivating need to complete
them.

## Over to you

And this is where I need help: Why don't you use Rapture? If you tried it, why
did you stop? What could be done to fix that? Join the Gitter channel and tell
me.

## The Manifesto

1. Make it easy to start using Rapture
2. Improve the website
3. Improve the documentation
4. Tidy up the issues
5. Roadmap

