package cowsay4s.core

import cowsay4s.core.impl.TalkingCow

trait CowSay {
  def talk(command: CowCommand): String
}

object CowSay {
  val default: CowSay = new CowSay {
    override def talk(command: CowCommand): String =
      TalkingCow.printToString(command)
  }

  def withTransformers(transformers: CommandTransformer*): CowSay = new CowSay {
    override def talk(command: CowCommand): String = {
      val transformedCmd = transformers.foldLeft(command)(
        (acc, transformer) => transformer(acc)
      )
      default.talk(transformedCmd)
    }
  }

  type CommandTransformer = CowCommand => CowCommand
}
