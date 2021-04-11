import mill.scalalib._

val scalafix = ivy"com.github.liancheng::organize-imports:0.4.4"

/** Enumerations. */
object enumeratum {
  val core = ivy"com.beachape::enumeratum::1.6.1"
  val playJson = ivy"com.beachape::enumeratum-play-json:1.6.3"
}

/** Tests. */
val scalatest = ivy"org.scalatest::scalatest::3.2.7"

/** Command-line args parsing. */
val scopt = ivy"com.github.scopt::scopt:4.0.1"

/** Web server (akka-http). */
object akka {
  private val akkaHttpVersion = "10.2.4"
  private val akkaVersion = "2.6.14"

  val stream = ivy"com.typesafe.akka::akka-stream:${akkaVersion}"
  val http = ivy"com.typesafe.akka::akka-http:${akkaHttpVersion}"
  val httpPlayJson = ivy"de.heikoseeberger::akka-http-play-json:1.36.0"
  val slf4j = ivy"com.typesafe.akka::akka-slf4j:${akkaVersion}"

  object testkit {
    val core = ivy"com.typesafe.akka::akka-testkit:${akkaVersion}"
    val stream = ivy"com.typesafe.akka::akka-stream-testkit:${akkaVersion}"
    val http = ivy"com.typesafe.akka::akka-http-testkit:${akkaHttpVersion}"
  }
}

/** HTML templating. */
val scalatags = ivy"com.lihaoyi::scalatags:0.9.4"

object database {
  val postgresql = ivy"org.postgresql:postgresql:42.2.19"
  val hikaricp = ivy"com.zaxxer:HikariCP:4.0.3"
}

/** Logging. */
object logging {
  val slf4jApi = ivy"org.slf4j:slf4j-api:1.7.30"
  val logback = ivy"ch.qos.logback:logback-classic:1.2.3"
  val log4s = ivy"org.log4s::log4s:1.9.0"
}

object apacheCommons {
  val text = ivy"org.apache.commons:commons-text:1.9"
  val codec = ivy"commons-codec:commons-codec:1.15"
}

/** Parser combinators. */
val fastparse = ivy"com.lihaoyi::fastparse:2.3.2"

/** Dependency injection. */
val macWire = ivy"com.softwaremill.macwire::macros:2.3.7"
