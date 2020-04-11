import mill.scalalib._

/** Enumerations. */
object enumeratum {
  val core = ivy"com.beachape::enumeratum::1.5.13"
  val playJson = ivy"com.beachape::enumeratum-play-json:1.5.16"
}

/** Tests. */
val scalatest = ivy"org.scalatest::scalatest::3.1.1"

/** Command-line args parsing. */
val scopt = ivy"com.github.scopt::scopt:3.7.1"

/** Web server (akka-http). */
object akka {
  private val akkaHttpVersion = "10.1.9"
  private val akkaVersion = "2.5.25"

  val stream = ivy"com.typesafe.akka::akka-stream:${akkaVersion}"
  val http = ivy"com.typesafe.akka::akka-http:${akkaHttpVersion}"
  val httpPlayJson = ivy"de.heikoseeberger::akka-http-play-json:1.27.0"
  val slf4j = ivy"com.typesafe.akka::akka-slf4j:${akkaVersion}"

  object testkit {
    val core = ivy"com.typesafe.akka::akka-testkit:${akkaVersion}"
    val stream = ivy"com.typesafe.akka::akka-stream-testkit:${akkaVersion}"
    val http = ivy"com.typesafe.akka::akka-http-testkit:${akkaHttpVersion}"
  }
}

/** HTML templating. */
val scalatags = ivy"com.lihaoyi::scalatags:0.7.0"

object database {
  val postgresql = ivy"org.postgresql:postgresql:42.2.6"
  val hikaricp = ivy"com.zaxxer:HikariCP:3.3.1"
}

/** Logging. */
object logging {
  val slf4jApi = ivy"org.slf4j:slf4j-api:1.7.28"
  val logback = ivy"ch.qos.logback:logback-classic:1.2.3"
  val log4s = ivy"org.log4s::log4s:1.8.2"
}

object apacheCommons {
  val text = ivy"org.apache.commons:commons-text:1.7"
  val codec = ivy"commons-codec:commons-codec:1.13"
}

/** Parser combinators. */
val fastparse = ivy"com.lihaoyi::fastparse:2.1.3"

/** Dependency injection. */
val macWire = ivy"com.softwaremill.macwire::macros:2.3.3"
