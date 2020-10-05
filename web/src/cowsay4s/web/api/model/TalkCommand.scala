package cowsay4s.web.api.model

import play.api.libs.json.{Json, OFormat}

import cowsay4s.core.{CowAction, CowCommand, MessageWrapping}
import cowsay4s.core.defaults.{DefaultCow, DefaultCowMode}

case class TalkCommand(message: String) {

  def toCowCommand: CowCommand = CowCommand(
    DefaultCow.Default,
    message,
    DefaultCowMode.Default,
    CowAction.CowSay,
    MessageWrapping(40),
  )
}

object TalkCommand {
  implicit val format: OFormat[TalkCommand] = Json.format
}
