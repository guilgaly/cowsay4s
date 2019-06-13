import mill.scalalib._

/** Command-line args parsing. */
val scopt = ivy"com.github.scopt::scopt:3.7.1"

/** Logging. */
object logging {
  val log4s = ivy"org.log4s::log4s:1.8.2"
  val slf4jApi = ivy"org.slf4j:slf4j-api:1.7.25"
  // logging to System.err
  val slf4jSimple = ivy"org.slf4j:slf4j-simple:1.7.25"
}

/** Enumerations. */
val enumeratum = ivy"com.beachape::enumeratum::1.5.13"

/** Tests. */
val scalatest = ivy"org.scalatest::scalatest::3.0.8"
