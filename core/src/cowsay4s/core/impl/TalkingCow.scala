package cowsay4s.core.impl

import cowsay4s.core.{CowAction, CowCommand, CowError, CowFace}

private[core] object TalkingCow {

  def printToString(command: CowCommand): Either[CowError, String] =
    for {
      cow <- Cow.load(command.provider)
      pimpedCow = pimpCow(cow, command.face, command.action)
      baloon = printBaloon(command: CowCommand)
      talkingCow = assembleTalkingCow(pimpedCow, baloon)
    } yield talkingCow

  private def pimpCow(cow: Cow, face: CowFace, action: CowAction): String = {
    val eyes = face.eyes.value
    val tongue = face.tongue.value
    val thoughts = action match {
      case CowAction.CowSay   => """\"""
      case CowAction.CowThink => "o"
    }

    doPimpCow(cow, eyes, tongue, thoughts)
  }

  private def doPimpCow(
      cow: Cow,
      eyes: String,
      tongue: String,
      thoughts: String
  ): String =
    cow.value
      .replace("$eyes", eyes)
      .replace("$tongue", tongue)
      .replace("$thoughts", thoughts)

  private def printBaloon(command: CowCommand) = {
    val delimiters = Baloon.Delimiters.fromAction(command.action)
    Baloon.format(command.message, command.wrap, delimiters)
  }

  private def assembleTalkingCow(pimpedCow: String, baloon: String) =
    baloon + pimpedCow
}
