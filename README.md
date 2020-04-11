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

## Web app (cowsay-online)

A web app is included in the module `web`, because we all know that of course
everything is better in the Cloud :cloud:. Written with Akka HTTP.

It aims to provide, in a single app:

- a webpage to manually generate cowsay outputs
- a RESTful API providing the same service
- Slack integration

Cowsay-online is accessible at
[cowsay-online.herokuapp.com](https://cowsay-online.herokuapp.com).

## Command line app

A command line app is included in the module `cli`. Run it with `--help` for
usage information.

## Getting started with the library

Cowsay4s is made up of three libraries:

- `cowsay4s-core` (which implements the Cowsay mechanics)
- `cowsay4s-defaults` (which provides a bunch of default "cows" and
"modes" to choose from)
- `cowsay4s-asciimojis` which adds support for "asciimojis" in the text
messages displayed by cowsay4s

Dependencies when building with Mill:

```scala
ivy"fr.ggaly::cowsay4s-core:0.2.2" // or ivy"fr.ggaly::cowsay4s-core::0.2.2" for Scala.js
ivy"fr.ggaly::cowsay4s-defaults:0.2.2" // or ivy"fr.ggaly::cowsay4s-defaults::0.2.2" for Scala.js
ivy"fr.ggaly::cowsay4s-asciimojis:0.2.2" // or ivy"fr.ggaly::cowsay4s-asciimojis::0.2.2" for Scala.js
```

Dependencies when building with SBT:

```scala
"fr.ggaly" %% "cowsay4s-core" % "0.2.2" // or "fr.ggaly" %%% "cowsay4s-core" % "0.2.2" for Scala.js
"fr.ggaly" %% "cowsay4s-defaults" % "0.2.2" // or "fr.ggaly" %%% "cowsay4s-defaults" % "0.2.2" for Scala.js
"fr.ggaly" %% "cowsay4s-asciimojis" % "0.2.2" // or "fr.ggaly" %%% "cowsay4s-asciimojis" % "0.2.2" for Scala.js
```

### Example with only the `core` module

Here is the example at the beginning of this README file, implemented
using only `cowsay4s-core` and providing a custom "cow" (ascii art):

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

val result: String = CowSay.default.talk(myCommand)

println(result)
```

And here is the same example, using some of the included defaults:

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

val result: String = CowSay.default.talk(myCommand)

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

## Using bitmap outputs

On the JVM platform only (not available on Scala.js), you can render a
cow as a bitmap image instead of plain text. You just need to
`import cowsay4s.core.BitmapCows._` to add the necessary methods to an
instance of `Cowsay`.

**A note on performance:** the first time your program renders a
bitmap cow, it may be pretty slow, presumably because of some
initialization being performed inside the Java graphics APIs. The
following renders should be much faster. On my computer, a simple
example can take about 1200 ms to render on the first iteration
and about 15 to 20 ms on following iterations.

### Bitmap output examples:

```scala
import java.awt.{Color, Font}
import java.awt.image.BufferedImage
import java.nio.file.Paths
import cowsay4s.core._
import cowsay4s.core.BitmapCows._

val cowsay = CowSay.default

val myCommand: CowCommand = ??? // Any CowCommand you want
val font = new Font("VT323", Font.PLAIN, 18)
val fontColor = Color.BLACK
// If you don't provide a background color, it will be kept transparent
val backgroundColor = Some(Color.WHITE)

// Output to a Java BufferedImage
val bufferedImage: BufferedImage =
  cowsay.talkToBufferedImage(command, font, fontColor, backgroundColor)

// Output to a PNG (in memory)
val pngBytes: Array[Byte] =
  cowsay.talkToPng(command, font, fontColor, backgroundColor)

// Output to a PNG file
val path = Paths.get("/tmp/cowsay.png")
cowsay.talkToPngFile(path, command, font, fontColor, backgroundColor)
```

## Using transformers

The `CowSay` implementation can be customized by the use of
`CommandTransformer`s, which are juste functions to transform the
commands received by the `CowSay` implementation.

Here's an example :

```scala
import cowsay4s.core._

val muteTransformer: CowSay.CommandTransformer =
  (command: CowCommand) => command.copy(message = "")
val thinkingTransformer: CowSay.CommandTransformer =
  (command: CowCommand) => command.copy(action = CowAction.CowThink)

// This CowSay will allways be thinking and mute, not matter what
// command you feed it.
val thinkingAndMuteCowSay =
  CowSay.withTransformers(thinkingTransformer, muteTransformer)
```
