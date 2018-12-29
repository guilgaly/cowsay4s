package cowsay4s.cli

import cowsay4s.core._

object Main {

  def main(args: Array[String]): Unit =
    CliArgs.parse(args) match {
      case Some(config) if config.list => executeList()
      case Some(config)                => execute(config)
      case None                        => // Do nothing and exit
    }

  private def executeList(): Unit = {
    Console.out.println(s"Available cowfiles names:")
    DefaultCow.cowNames.foreach { cowName =>
      Console.out.println(s"- $cowName")
    }
  }

  private def execute(config: CliArgs): Unit = {
    val message = config.message.getOrElse("")
    val eyes = config.eyes.getOrElse(config.mode.face.eyes.value)
    val tongue = config.tongue.getOrElse(config.mode.face.tongue.value)
    val command = CowCommand(
      config.action,
      config.cow,
      CowFace(eyes, tongue),
      StrictPositiveInt(config.wrapcolumn),
      message
    )

    val cowsayOutput = CowSay.withCustomCommand(command)

    Console.out.println(cowsayOutput)
  }
}
