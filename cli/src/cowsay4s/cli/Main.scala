package cowsay4s.cli

import cowsay4s.asciimojis.AsciimojisTransformer
import cowsay4s.core._
import cowsay4s.core.defaults.DefaultCow

object Main {
  def main(args: Array[String]): Unit =
    CliArgs.parse(args.toSeq) match {
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
    val eyes = config.eyes.getOrElse(config.mode.eyes)
    val tongue = config.tongue.getOrElse(config.mode.tongue)
    val command = CowCommand(
      config.cow,
      message,
      eyes,
      tongue,
      config.action,
      MessageWrapping(config.wrapcolumn)
    )

    val cowsay = CowSay.withTransformers(AsciimojisTransformer)
    val cowsayOutput = cowsay.talk(command)

    Console.out.println(cowsayOutput)
  }
}
