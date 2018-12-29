import $file.dependencies
import $file.settings
import mill._
import mill.scalalib._
import mill.scalajslib._
import mill.scalalib.scalafmt.ScalafmtModule

trait CommonModule extends ScalaModule {
  def platformSegment: String

  override def sources = T.sources(
    millSourcePath / "src",
    millSourcePath / s"src-$platformSegment"
  )
}

trait CoreModule
    extends CommonModule
    with CrossScalaModule
    with ScalafmtModule {
  override def scalacOptions = settings.scalacOptions(crossScalaVersion)
  override def repositories = super.repositories ++ settings.customRepositories

  override def ivyDeps = Agg(dependencies.enumeratum)

  override def millSourcePath = build.millSourcePath / "core"

  trait CoreTestsModule extends Tests with CommonModule {
    override def platformSegment = CoreModule.this.platformSegment
    override def testFrameworks = Seq("org.scalatest.tools.Framework")
    override def ivyDeps = Agg(dependencies.scalatest)
  }
}

object core extends Module {

  object jvm extends Cross[JvmCoreModule](settings.scalaVersion.cross: _*)
  class JvmCoreModule(val crossScalaVersion: String) extends CoreModule {
    override def platformSegment = "jvm"

    object test extends CoreTestsModule
  }

  object js extends Cross[JsCoreModule](settings.scalaVersion.cross: _*)
  class JsCoreModule(val crossScalaVersion: String)
      extends CoreModule
      with ScalaJSModule {
    override def platformSegment = "js"
    override def scalaJSVersion = settings.scalaJsVersion.default

    object test extends CoreTestsModule with TestScalaJSModule {
      def scalaJSVersion = settings.scalaJsVersion.default
    }
  }
}

object cli extends ScalaModule with ScalafmtModule {
  override def scalaVersion = settings.scalaVersion.default
  override def scalacOptions =
    settings.scalacOptions(settings.scalaVersion.default)
  override def moduleDeps = Seq(core.jvm(settings.scalaVersion.default))
  override def ivyDeps = Agg(
    dependencies.scopt,
  )

  object test extends Tests with ScalafmtModule {
    override def testFrameworks = Seq("org.scalatest.tools.Framework")
    override def ivyDeps = Agg(dependencies.scalatest)
  }
}
