import $file.cowgen
import $file.dependencies
import $file.settings
import mill._
import mill.scalajslib._
import mill.scalalib._
import mill.scalalib.publish.{Developer, License, PomSettings, VersionControl}
import mill.scalalib.scalafmt.ScalafmtModule

trait CommonModule extends ScalaModule with ScalafmtModule {
  def platformSegment: String

  override def sources = T.sources(
    millSourcePath / "src",
    millSourcePath / s"src-$platformSegment"
  )
}

trait CoreModule extends CommonModule with CrossScalaModule with PublishModule {

  override def scalacOptions = settings.scalacOptions(crossScalaVersion)
  override def repositories = super.repositories ++ settings.customRepositories

  override def ivyDeps = Agg(dependencies.enumeratum)

  override def millSourcePath: os.Path = build.millSourcePath / 'core

  override def artifactName = "cowsay4s-core"
  override def publishVersion = "0.1.2-SNAPSHOT"
  override def pomSettings = PomSettings(
    description = "Cowsay implemented as a Scala library",
    organization = "fr.ggaly",
    url = "https://github.com/guilgaly/cowsay4s",
    licenses = Seq(License.MIT),
    versionControl = VersionControl.github("guilgaly", "cowsay4s"),
    developers = Seq(
      Developer("guilgaly", "Guillaume Galy", "https://github.com/guilgaly")
    )
  )

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
