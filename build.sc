import $ivy.`com.goyeau::mill-scalafix:0.2.1`
import $ivy.`com.lihaoyi::mill-contrib-buildinfo:0.9.6`

import $file.cowgen
import $file.dependencies
import $file.projectVersion
import $file.settings

import mill._
import mill.api.Strict
import mill.contrib.buildinfo.BuildInfo
import mill.eval.Evaluator
import mill.define.Task
import mill.scalajslib._
import mill.scalalib._
import mill.scalalib.publish.{Developer, License, PomSettings, VersionControl}
import mill.scalalib.scalafmt.ScalafmtModule

import com.goyeau.mill.scalafix.ScalafixModule

def format(ev: Evaluator) = T.command {
  def findAllChildren(module: Module): Seq[Module] = {
    val children = module.millModuleDirectChildren
    if (children.isEmpty) Seq(module)
    else module +: children.flatMap(findAllChildren)
  }

  def eval[T](e: Task[T]): T =
    ev.evaluate(Strict.Agg(e)).values match {
      case Seq()     => throw new NoSuchElementException
      case Seq(e: T) => e
    }

  findAllChildren(ev.rootModule)
    .collect { case mod: ScalafmtModule with ScalafixModule => mod }
    .foreach { mod =>
      // We don't currently have different sources for specific Scala versions
      if (eval(mod.scalaVersion) == settings.scalaVersion.default) {
        println(s"Formatting module $mod...")
        eval(mod.fix()) // Organize imports
        eval(mod.reformat()) // Scalafmt
      }
    }
}

// *************** Base traits ***************

trait BaseModule extends ScalaModule with ScalafmtModule with ScalafixModule {
  def platformSegment: String

  override def repositoriesTask = T.task {
    super.repositoriesTask() ++ settings.customRepositories
  }

  override def scalacOptions = T { settings.scalacOptions(scalaVersion()) }

  override def scalafixIvyDeps = Agg(dependencies.scalafix)

  override def sources = T.sources(
    millSourcePath / "src",
    millSourcePath / s"src-$platformSegment",
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
    override def ivyDeps = Agg(dependencies.enumeratum.core)

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
        Developer("guilgaly", "Guillaume Galy", "https://github.com/guilgaly"),
      ),
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
  override def moduleDeps = Seq(core.jvm(scalaVers))
  override def ivyDeps = Agg(
    dependencies.scopt,
  )

  object test extends Tests with ScalafmtModule {
    def testFrameworks = Seq("org.scalatest.tools.Framework")
    override def moduleDeps =
      super.moduleDeps :+ testutils.jvm(settings.scalaVersion.default)
    override def ivyDeps = Agg(dependencies.scalatest)
  }
}

// *************** Web application module ***************

object web
  extends ScalaModule
    with ScalafmtModule
    with ScalafixModule
    with BuildInfo {
  private def scalaVers = settings.scalaVersion.default

  def scalaVersion = scalaVers
  override def scalacOptions = settings.scalacOptions(scalaVers)
  override def moduleDeps = Seq(core.jvm(scalaVers))
  override def ivyDeps = Agg(
    dependencies.akka.stream,
    dependencies.akka.http,
    dependencies.akka.httpPlayJson,
    dependencies.akka.slf4j,
    dependencies.scalatags,
    dependencies.database.postgresql,
    dependencies.database.hikaricp,
    dependencies.logging.slf4jApi,
    dependencies.logging.logback,
    dependencies.logging.log4s,
    dependencies.enumeratum.playJson,
    dependencies.apacheCommons.text,
    dependencies.apacheCommons.codec,
    dependencies.fastparse,
    dependencies.macWire,
  )

  override def scalafixIvyDeps = Agg(dependencies.scalafix)

  def publishVersion = "0.1.4-SNAPSHOT"

  override def buildInfoMembers: T[Map[String, String]] = T {
    Map(
      "name" -> "cowsay-online",
      "version" -> publishVersion,
      "scalaVersion" -> scalaVersion(),
    )
  }
  override def buildInfoPackageName = Some("cowsay4s.web")
  override def buildInfoObjectName = "BuildInfo"

  object test extends Tests with ScalafmtModule {
    override def testFrameworks = Seq("org.scalatest.tools.Framework")
    override def moduleDeps =
      super.moduleDeps :+ testutils.jvm(settings.scalaVersion.default)
    override def ivyDeps = Agg(
      dependencies.scalatest,
      dependencies.akka.testkit.core,
      dependencies.akka.testkit.stream,
      dependencies.akka.testkit.http,
    )
  }
}
