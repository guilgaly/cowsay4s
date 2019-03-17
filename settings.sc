val customRepositories = Seq()

object scalaVersion {

  val v2_11 = "2.11.12"
  val v2_12 = "2.12.8"
  val v2_13 = "2.13.0-M5"

  val default = v2_12
  val cross = Seq(v2_11, v2_12, v2_13)
}

object scalaJsVersion {
  val default = "0.6.26"
}

/**
 * See https://tpolecat.github.io/2017/04/25/scalac-flags.html
 */
def scalacOptions(scalaVersion: String) = {
  def defaultScalacOptions = Seq(
    "-encoding",
    "utf-8", // Specify character encoding used by source files.
    "-explaintypes", // Explain type errors in more detail.
    "-feature", // Emit warning and location for usages of features that should be imported explicitly.
    "-deprecation",
    //  "-language:existentials",            // Existential types (besides wildcard types) can be written and inferred
    //  "-language:experimental.macros",     // Allow macro definition (besides implementation and application)
    //  "-language:higherKinds",             // Allow higher-kinded types
    //  "-language:implicitConversions",     // Allow definition of implicit functions called views
    "-unchecked", // Enable additional warnings where generated code depends on assumptions.
    "-Xcheckinit", // Wrap field accessors to throw an exception on uninitialized access.
    //  "-Xfatal-warnings", // Fail the compilation if there are any warnings.
    //  "-Xfuture",                          // Turn on future language features.
    "-Ywarn-dead-code", // Warn when dead code is identified.
    "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
    "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
    "-Xlint:nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
    "-Ywarn-numeric-widen", // Warn when numerics are widened.
    "-Ywarn-value-discard", // Warn when non-Unit expression results are unused.
  )

  def v2_11 = defaultScalacOptions ++ Seq(
    "-Xsource:2.12",
    "-Ypartial-unification", // Enable partial unification in type constructor inference
    "-Yno-adapted-args", // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
    // "-Ywarn-adapted-args", // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
  )

  def v2_12and2_13 = defaultScalacOptions ++ Seq(
    "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
    "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
    "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
    "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
    "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
    "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
    //  "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
    "-Xlint:nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
    "-Xlint:option-implicit", // Option.apply used implicit view.
    "-Xlint:package-object-classes", // Class or object defined in package object.
    "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
    "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
    "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
    "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
    "-Ywarn-extra-implicit", // Warn when more than one implicit parameter section is defined. NOT SUPPORTED IN 2.11
    "-Ywarn-unused:implicits", // Warn if an implicit parameter is unused. NOT SUPPORTED IN 2.11
    "-Ywarn-unused:imports", // Warn if an import selector is not referenced. NOT SUPPORTED IN 2.11
    "-Ywarn-unused:locals", // Warn if a local definition is unused. NOT SUPPORTED IN 2.11
    "-Ywarn-unused:params", // Warn if a value parameter is unused. NOT SUPPORTED IN 2.11
    "-Ywarn-unused:patvars", // Warn if a variable bound in a pattern is unused. NOT SUPPORTED IN 2.11
    "-Ywarn-unused:privates", // Warn if a private member is unused. NOT SUPPORTED IN 2.11
    "-Ywarn-macros:after", //  NOT SUPPORTED IN 2.11
    "-Ybackend-parallelism", //  NOT SUPPORTED IN 2.11
    "8",
  )

  def v2_12 = v2_12and2_13 ++ Seq(
    "-Ypartial-unification", // Enable partial unification in type constructor inference. REMOVED IN 2.13
    "-Yno-adapted-args", // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver. REMOVED IN 2.13
    // "-Ywarn-adapted-args", // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
    "-Xlint:by-name-right-associative", // By-name parameter of right associative operator. REMOVED IN 2.13
    "-Xlint:unsound-match", // Pattern match may not be typesafe. REMOVED IN 2.13
  )

  def v2_13 = v2_12and2_13 ++ Seq(
  )

  if (scalaVersion.startsWith("2.11")) v2_11
  else if (scalaVersion.startsWith("2.12")) v2_12
  else if (scalaVersion.startsWith("2.13")) v2_13
  else defaultScalacOptions
}
