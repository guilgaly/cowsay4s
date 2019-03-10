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

trait BasePublishCrossModule
    extends BaseModule
    with CrossScalaModule
    with PublishModule {
  def moduleName: String
  def moduleDescription: String

  override def millSourcePath: os.Path = build.millSourcePath / moduleName

  override def ivyDeps = Agg(dependencies.enumeratum)
  override def repositories = super.repositories ++ settings.customRepositories
  override def scalacOptions = settings.scalacOptions(crossScalaVersion)

  override def publishVersion = projectVersion.projectVersion
  override def artifactName = s"cowsay4s-$moduleName"
  override def pomSettings = PomSettings(
    description = moduleDescription,
    organization = "fr.ggaly",
    url = "https://github.com/guilgaly/cowsay4s",
    licenses = Seq(License.MIT),
    versionControl = VersionControl.github("guilgaly", "cowsay4s"),
    developers = Seq(
      Developer("guilgaly", "Guillaume Galy", "https://github.com/guilgaly")
    )
  )

  trait Tests extends super.Tests with BaseModule {
    override def platformSegment = BasePublishCrossModule.this.platformSegment
    override def testFrameworks = Seq("org.scalatest.tools.Framework")
    override def ivyDeps = Agg(dependencies.scalatest)
  }
}

trait LibraryModule extends Module {
  def moduleName: String
  def moduleDescription: String
  def moduleDeps: Seq[LibraryModule] = Seq.empty

  def jvm: Cross[_ <: JvmModule]
  def js: Cross[_ <: JsModule]

  class JvmModule(val crossScalaVersion: String) extends CommonModule {
    override def platformSegment = "jvm"
    override def moduleDeps = LibraryModule.this.moduleDeps.map(_.jvm())

    object test extends super.Tests {
      override def moduleDeps = super.moduleDeps :+ testutils.jvm()
    }
  }

  class JsModule(val crossScalaVersion: String)
      extends ScalaJSModule
      with CommonModule {
    override def platformSegment = "js"
    override def moduleDeps = LibraryModule.this.moduleDeps.map(_.js())
    override def scalaJSVersion = settings.scalaJsVersion.default

    object test extends super.Tests with TestScalaJSModule {
      override def scalaJSVersion = settings.scalaJsVersion.default
      override def moduleDeps = super.moduleDeps :+ testutils.js()
    }
  }

  trait CommonModule extends BasePublishCrossModule {
    override def moduleName = LibraryModule.this.moduleName
    override def moduleDescription = LibraryModule.this.moduleDescription
  }
}

// *************** Common utilities modules ***************

object testutils extends Module {

  object jvm extends Cross[JvmTestutilsModule](settings.scalaVersion.cross: _*)
  class JvmTestutilsModule(val crossScalaVersion: String)
      extends TestutilsModule {
    override def platformSegment = "jvm"
  }

  object js extends Cross[JsTestutilsModule](settings.scalaVersion.cross: _*)
  class JsTestutilsModule(val crossScalaVersion: String)
      extends TestutilsModule
      with ScalaJSModule {
    override def platformSegment = "js"
    override def scalaJSVersion = settings.scalaJsVersion.default
  }

  trait TestutilsModule extends BaseModule with CrossScalaModule {
    override def millSourcePath: os.Path = build.millSourcePath / "testutils"
    override def ivyDeps = Agg(dependencies.scalatest)
  }
}

// *************** Libraries modules ***************

object core extends LibraryModule {
  override def moduleName = "core"
  override def moduleDescription = "Cowsay implemented as a Scala library"

  override object jvm extends Cross[JvmModule](settings.scalaVersion.cross: _*)
  override object js extends Cross[JsModule](settings.scalaVersion.cross: _*)
}

object defaults extends LibraryModule {
  override def moduleName = "defaults"
  override def moduleDescription =
    "Default cows and modes for use with cowsay4s-core"
  override def moduleDeps = Seq(core)

  object jvm extends Cross[JvmDefaultsModule](settings.scalaVersion.cross: _*)
  class JvmDefaultsModule(crossScalaVersion: String)
      extends JvmModule(crossScalaVersion)
      with DefaultsModule

  object js extends Cross[JsDefaultsModule](settings.scalaVersion.cross: _*)
  class JsDefaultsModule(crossScalaVersion: String)
      extends JsModule(crossScalaVersion)
      with DefaultsModule

  trait DefaultsModule extends CommonModule {
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

    override def generatedSources = T {
      val cowfiles = allCowfiles().map(_.path)
      val dir = T.ctx().dest
      cowgen.generateDefaultCows(dir, cowfiles)
      Seq(PathRef(dir))
    }
  }
}

object asciimojis extends LibraryModule {
  override def moduleName = "asciimojis"
  override def moduleDescription = "Asciimojis support for cowsay4s-core"
  override def moduleDeps = Seq(core)

  override object jvm extends Cross[JvmModule](settings.scalaVersion.cross: _*)
  override object js extends Cross[JsModule](settings.scalaVersion.cross: _*)
}

// *************** CLI application module ***************

object cli extends ScalaModule with ScalafmtModule {
  private def scalaVers = settings.scalaVersion.default

  override def scalaVersion = scalaVers
  override def scalacOptions = settings.scalacOptions(scalaVers)
  override def moduleDeps = Seq(
    defaults.jvm(scalaVers),
    asciimojis.jvm(scalaVers),
  )
  override def ivyDeps = Agg(
    dependencies.scopt,
  )

  object test extends Tests with ScalafmtModule {
    override def testFrameworks = Seq("org.scalatest.tools.Framework")
    override def moduleDeps =
      super.moduleDeps :+ testutils.jvm(settings.scalaVersion.default)
    override def ivyDeps = Agg(dependencies.scalatest)
  }
}
