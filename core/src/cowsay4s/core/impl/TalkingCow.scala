package cowsay4s.core.impl

import cowsay4s.core._

private[core] object TalkingCow {
  def printToString(command: CowCommand): String = {
    val pimpedCow =
      pimpCow(command.cow, command.eyes, command.tongue, command.action)
    val baloon = printBaloon(command: CowCommand)
    assembleCow(pimpedCow, baloon)
  }

  private def pimpCow(
      cow: Cow,
      eyes: CowEyes,
      tongue: CowTongue,
      action: CowAction
  ): String = {
    val thoughts = action match {
      case CowAction.CowSay   => """\"""
      case CowAction.CowThink => "o"
    }

    doPimpCow(cow, eyes.value, tongue.value, thoughts)
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

  private def assembleCow(pimpedCow: String, baloon: String) =
    baloon + pimpedCow
}
