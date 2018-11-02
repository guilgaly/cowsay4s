package cowsay4s.core

import cowsay4s.core.impl.TalkingCow

object CowSay {

  def withCustomCommand(command: CowCommand): Either[CowError, String] =
    TalkingCow.printToString(command)
}
