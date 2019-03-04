package cowsay4s.core

import cowsay4s.core.impl.TalkingCow

object CowSay {

  def talk(command: CowCommand): String =
    TalkingCow.printToString(command)
}
