import $file.cowgen
import $file.dependencies
import $file.projectVersion
import $file.settings
import mill._
import mill.scalajslib._
import mill.scalalib._
import mill.scalalib.publish.{Developer, License, PomSettings, VersionControl}
import mill.scalalib.scalafmt.ScalafmtModule

// *************** Base traits ***************

trait BaseModule extends ScalaModule with ScalafmtModule {
  def platformSegment: String

  override def sources = T.sources(
    millSourcePath / "src",
    millSourcePath / s"src-$platformSegment"
  )
}

// *************** Common utilities modules ***************

object testutils extends Module {

  object jvm extends Cross[JvmTestutilsModule](settings.scalaVersion.cross: _*)
  class JvmTestutilsModule(val crossScalaVersion: String)
      extends TestutilsModule {
    def platformSegment = "jvm"
  }

  object js extends Cross[JsTestutilsModule](settings.scalaVersion.cross: _*)
  class JsTestutilsModule(val crossScalaVersion: String)
      extends TestutilsModule
      with ScalaJSModule {
    def platformSegment = "js"
    def scalaJSVersion = settings.scalaJsVersion.default
  }

  trait TestutilsModule extends BaseModule with CrossScalaModule {
    override def millSourcePath: os.Path = build.millSourcePath / "testutils"
    override def ivyDeps = Agg(dependencies.scalatest)
  }
}

// *************** Library modules ***************

object core extends Module {
  object jvm extends Cross[JvmCoreModule](settings.scalaVersion.cross: _*)
  class JvmCoreModule(val crossScalaVersion: String) extends CoreModule {
    def platformSegment = "jvm"

    object test extends super.Tests {
      override def moduleDeps = super.moduleDeps :+ testutils.jvm()
    }
  }

  object js extends Cross[JsCoreModule](settings.scalaVersion.cross: _*)
  class JsCoreModule(val crossScalaVersion: String)
      extends ScalaJSModule
      with CoreModule {
    def platformSegment = "js"
    def scalaJSVersion = settings.scalaJsVersion.default

    object test extends super.Tests with TestScalaJSModule {
      def scalaJSVersion = settings.scalaJsVersion.default
      override def moduleDeps = super.moduleDeps :+ testutils.js()
    }
  }

  trait CoreModule extends BaseModule with CrossScalaModule with PublishModule {
    outer =>
    def moduleName = "core"
    def moduleDescription = "Cowsay implemented as a Scala library"

    override def millSourcePath: os.Path = build.millSourcePath / moduleName
    override def ivyDeps = Agg(dependencies.enumeratum)
    override def repositories =
      super.repositories ++ settings.customRepositories
    override def scalacOptions = settings.scalacOptions(crossScalaVersion)

    // Publish settings
    def publishVersion = projectVersion.projectVersion
    def artifactName = s"cowsay4s-$moduleName"
    def pomSettings = PomSettings(
      description = moduleDescription,
      organization = "fr.ggaly",
      url = "https://github.com/guilgaly/cowsay4s",
      licenses = Seq(License.MIT),
      versionControl = VersionControl.github("guilgaly", "cowsay4s"),
      developers = Seq(
        Developer("guilgaly", "Guillaume Galy", "https://github.com/guilgaly")
      )
    )

    // Cowfiles code generation
    def cowfiles = T.sources { millSourcePath / 'cowfiles }
    def allCowfiles = T {
      def isHiddenFile(path: os.Path) = path.last.startsWith(".")
      for {
        root <- cowfiles()
        if os.exists(root.path)
        path <- if (os.isDir(root.path)) os.walk(root.path) else Seq(root.path)
        if os.isFile(path) && path.ext == "cow" && !isHiddenFile(path)
      } yield PathRef(path)
    }
    def generatedSources = T {
      val cowfiles = allCowfiles().map(_.path)
      val dir = T.ctx().dest
      cowgen.generateDefaultCows(dir, cowfiles)
      Seq(PathRef(dir))
    }

    trait Tests extends super.Tests with BaseModule {
      def platformSegment = outer.platformSegment
      def testFrameworks = Seq("org.scalatest.tools.Framework")
      override def ivyDeps = Agg(dependencies.scalatest)
    }
  }
}

// *************** CLI application module ***************

object cli extends ScalaModule with ScalafmtModule {
  private def scalaVers = settings.scalaVersion.default

  def scalaVersion = scalaVers
  override def scalacOptions = settings.scalacOptions(scalaVers)
  override def moduleDeps = Seq(core.jvm(scalaVers))
  override def ivyDeps = Agg(
    dependencies.scopt
  )

  object test extends Tests with ScalafmtModule {
    def testFrameworks = Seq("org.scalatest.tools.Framework")
    override def moduleDeps =
      super.moduleDeps :+ testutils.jvm(settings.scalaVersion.default)
    override def ivyDeps = Agg(dependencies.scalatest)
  }
}
