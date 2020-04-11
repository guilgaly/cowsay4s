package cowsay4s.web.api

import cowsay4s.core.CowSay
import cowsay4s.web.api.model.{TalkCommand, TalkResponse}

final class ApiCowsay(cowSay: CowSay) {

  def talk(talkCommand: TalkCommand): TalkResponse = {
    val cowCommand = talkCommand.toCowCommand
    val theCowSaid = cowSay.talk(cowCommand)
    TalkResponse(theCowSaid)
  }
}
