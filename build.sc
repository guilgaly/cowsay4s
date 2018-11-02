import $file.dependencies
import $file.settings
import mill._
import mill.scalalib._
import mill.scalalib.scalafmt.ScalafmtModule

trait Cowsay4sModule extends ScalaModule with ScalafmtModule {
  override def scalaVersion = settings.scalaVersion
  override def scalacOptions = settings.defaultScalacOptions
  override def repositories = super.repositories ++ settings.customRepositories

  object test extends Tests with ScalafmtModule {
    override def moduleDeps =
      if (this == common.test) super.moduleDeps
      else super.moduleDeps ++ Seq(common.test)
    override def ivyDeps = Agg(
      dependencies.scalatest,
      dependencies.logging.slf4jSimple,
    )
    override def testFrameworks = Seq("org.scalatest.tools.Framework")
  }
}

object core extends Cowsay4sModule {
  override def scalaVersion = settings.scalaVersion
  override def scalacOptions = settings.defaultScalacOptions
}

object common extends Cowsay4sModule {
  override def ivyDeps = Agg(
    dependencies.logging.log4s,
    dependencies.logging.slf4jApi,
    dependencies.enumeratum,
  )
}
