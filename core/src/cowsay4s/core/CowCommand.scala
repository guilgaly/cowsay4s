package cowsay4s.core

case class CowCommand(
    action: CowAction,
    cow: Cow,
    message: String,
    eyes: CowEyes = CowEyes.default,
    tongue: CowTongue = CowTongue.default,
    wrap: MessageWrapping = MessageWrapping.default)

object CowCommand {

  def apply(
      action: CowAction,
      cow: Cow,
      message: String,
      mode: CowMode): CowCommand =
    CowCommand(action, cow, message, mode.eyes, mode.tongue)

  def apply(
      action: CowAction,
      cow: Cow,
      message: String,
      mode: CowMode,
      wrap: MessageWrapping): CowCommand =
    CowCommand(action, cow, message, mode.eyes, mode.tongue, wrap)
}
