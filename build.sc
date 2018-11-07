import $file.dependencies
import $file.settings
import mill._
import mill.scalalib._
import mill.scalalib.scalafmt.ScalafmtModule

object core
    extends Cross[CoreModule](settings.scalaVersion.v2_11,
                              settings.scalaVersion.v2_12)
class CoreModule(val crossScalaVersion: String)
    extends CrossScalaModule
    with ScalafmtModule {
  override def scalacOptions = settings.scalacOptions(crossScalaVersion)
  override def repositories = super.repositories ++ settings.customRepositories

  override def ivyDeps = Agg(
    dependencies.logging.log4s,
    dependencies.logging.slf4jApi,
    dependencies.enumeratum,
  )

  object test extends Tests with ScalafmtModule {
    override def moduleDeps = super.moduleDeps :+ testCommon(crossScalaVersion)
    override def testFrameworks = Seq("org.scalatest.tools.Framework")
  }
}

object cli extends ScalaModule with ScalafmtModule {
  override def scalaVersion = settings.scalaVersion.default
  override def scalacOptions =
    settings.scalacOptions(settings.scalaVersion.default)
  override def moduleDeps = Seq(core(settings.scalaVersion.default))
  override def ivyDeps = Agg(
    dependencies.scopt,
  )

  object test extends Tests with ScalafmtModule {
    override def moduleDeps =
      super.moduleDeps :+ testCommon(settings.scalaVersion.default)
    override def testFrameworks = Seq("org.scalatest.tools.Framework")
  }
}

object testCommon
    extends Cross[TestCommonModule](settings.scalaVersion.v2_11,
                                    settings.scalaVersion.v2_12)
class TestCommonModule(val crossScalaVersion: String)
    extends ScalaModule
    with ScalafmtModule
    with CrossScalaModule {
  override def scalacOptions = settings.scalacOptions(crossScalaVersion)

  override def ivyDeps = Agg(
    dependencies.scalatest,
    dependencies.logging.slf4jSimple,
  )
}
