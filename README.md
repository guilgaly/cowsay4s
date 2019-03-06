[![CircleCI](https://circleci.com/gh/guilgaly/cowsay4s/tree/master.svg?style=svg)](https://circleci.com/gh/guilgaly/cowsay4s/tree/master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/61283f92a63444738407f767d6bf86f7)](https://www.codacy.com/app/guilgaly/cowsay4s?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=guilgaly/cowsay4s&amp;utm_campaign=Badge_Grade)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/fr.ggaly/cowsay4s-core_2.12/badge.svg)](https://search.maven.org/search?q=g:fr.ggaly%20AND%20a:cowsay4s*)

# cowsay4s

A Scala implementation of cowsay, written from scratch, but of course inspired
by the original cowsay program written by Tony Monroe.

```text
 _______________
< Cows ♥ Scala! >
 ---------------
        \   ^__^
         \  (oo)\_______
            (__)\       )\/\
                ||----w |
                ||     ||
```

See also:

- [Wikipedia](https://en.wikipedia.org/wiki/Cowsay) for more info on cowsay
- [tnalpgge/rank-amateur-cowsay](https://github.com/tnalpgge/rank-amateur-cowsay)
for the original program

## Getting started with the library

Cowsay4s is made up of two libraries: `cowsay4s-core` (which implements
the Cowsay mechanics) and `cowsay4s-defaults` (which provides a bunch of
default "cows" and "modes" to choose from).

Dependencies when building with Mill:

```scala
ivy"fr.ggaly::cowsay4s-core:0.2.0" // or ivy"fr.ggaly::cowsay4s-core::0.2.0" for Scala.js
ivy"fr.ggaly::cowsay4s-defaults:0.2.0" // or ivy"fr.ggaly::cowsay4s-defaults::0.2.0" for Scala.js
```

Dependencies when building with SBT:

```scala
"fr.ggaly" %% "cowsay4s-core" % "0.2.0" // or "fr.ggaly" %%% "cowsay4s-core" % "0.2.0" for Scala.js
"fr.ggaly" %% "cowsay4s-defaults" % "0.2.0" // or "fr.ggaly" %%% "cowsay4s-defaults" % "0.2.0" for Scala.js
```

### Example with only the `core` module

Here is the example at the beginning of this README file, implemented
using only `cowsay4s-core`:

```scala
import cowsay4s.core._

val myCustomCow: Cow = CustomCow(
  """
    |        $thoughts   ^__^
    |         $thoughts  ($eyes)\\_______
    |            (__)\\       )\\/\\
    |             $tongue ||----w |
    |                ||     ||
  """.stripMargin
)

val myCommand: CowCommand = CowCommand(
  cow = myCustomCow,
  message = "Cows ♥ Scala!",
  eyes = CowEyes.default, // optional, defaults to 'CowEyes.default' (same as this example)
  tongue = CowTongue.default, // optional, defaults to 'CowTongue.default' (same as this example)
  action = CowAction.CowSay, // Optional, defaults to CowAction.defaultValue (same as this example)
  wrap = MessageWrapping(40) // optional, defaults to 'MessageWrapping.default' (same as this example)
)

val result: String = CowSay.talk(myCommand)

println(result)
```

### Example with the `defaults` module

And here is the same example, implemented using `cowsay4s-core` and
`cowsay4s-defaults`:

```scala
import cowsay4s.core._
import cowsay4s.defaults.{DefaultCow, DefaultCowMode}

val myCommand: CowCommand = CowCommand(
  cow = DefaultCow.Default, // There are many other cows to choose from in 'DefaultCow'
  message = "Cows ♥ Scala!",
  mode = DefaultCowMode.Default, // Optional, defaults to DefaultCowMode.defaultValue (same as this example)
  action = CowAction.CowSay, // Optional, defaults to CowAction.defaultValue (same as this example)
  wrap = MessageWrapping(40) // optional, defaults to 'MessageWrapping.default' (same as this example)
)

val result: String = CowSay.talk(myCommand)

println(result)
```

### Parameters description

The `action` determines wether the cow "says" or "thinks" the provided
`message` (the speech bubble is formatted differently).

The `cow` provides the drawing below the speech bubble.

The `eyes` and `tongue` values replace the `$eyes` and `$tongue`
placeholders in the cow (when they exist; many cows lack those
placeholders). They can also be provided together as a single parameter,
called `mode`. 

`wrap` determines the maximum width of the text before it gets wrapped
with line breaks. This works reasonably well with most unicode
characters on the JVM, less so if you use the Scala.js variant.
