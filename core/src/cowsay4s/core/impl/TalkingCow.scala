package cowsay4s.core.impl

import cowsay4s.core._

private[core] object TalkingCow {

  def printToString(command: CowCommand): String = {
    val pimpedCow = pimpCow(command.cow, command.face, command.action)
    val baloon = printBaloon(command: CowCommand)
    assembleTalkingCow(pimpedCow, baloon)
  }

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
    cow.cowValue
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
