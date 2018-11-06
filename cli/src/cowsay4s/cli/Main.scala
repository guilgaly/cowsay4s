package cowsay4s.cli

import cowsay4s.core._

object Main {

  def main(args: Array[String]): Unit =
    CliArgs.parse(args) match {
      case Some(config) => execute(config)
      case None         => // Do nothing and exit
    }

  private def execute(config: CliArgs): Unit =
    if (config.list) {
      Console.err.println("list option is not supported yet")
    } else {
      val message = config.message.getOrElse("")
      val eyes = config.eyes.getOrElse(config.mode.face.eyes.value)
      val tongue = config.tongue.getOrElse(config.mode.face.tongue.value)
      val command = CowCommand(
        config.action,
        CowName(config.cowfile),
        CowFace(eyes, tongue),
        StrictPositiveInt(config.wrapcolumn),
        message
      )

      CowSay.withCustomCommand(command) match {
        case Left(err)    => Console.err.println(err.printError)
        case Right(value) => Console.out.println(value)
      }
    }
}
