package cowsay4s.cli

object Main {
  def main(args: Array[String]): Unit =
    CliArgs.parse(args) match {
      case Some(config) => println(config)
      case None         => // Do nothing and exit
    }
}
