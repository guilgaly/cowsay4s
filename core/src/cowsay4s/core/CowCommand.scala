package cowsay4s.core

case class CowCommand(
    cow: Cow,
    message: String,
    eyes: CowEyes = CowEyes.default,
    tongue: CowTongue = CowTongue.default,
    action: CowAction = CowAction.defaultValue,
    wrap: MessageWrapping = MessageWrapping.default
)

object CowCommand {

  def apply(cow: Cow, message: String, mode: CowMode): CowCommand =
    CowCommand(cow, message, mode.eyes, mode.tongue)

  def apply(
      cow: Cow,
      message: String,
      mode: CowMode,
      action: CowAction
  ): CowCommand =
    CowCommand(cow, message, mode.eyes, mode.tongue, action)

  def apply(
      cow: Cow,
      message: String,
      mode: CowMode,
      wrap: MessageWrapping
  ): CowCommand =
    CowCommand(cow, message, mode.eyes, mode.tongue, wrap = wrap)

  def apply(
      cow: Cow,
      message: String,
      mode: CowMode,
      action: CowAction,
      wrap: MessageWrapping
  ): CowCommand =
    CowCommand(cow, message, mode.eyes, mode.tongue, action, wrap)
}
