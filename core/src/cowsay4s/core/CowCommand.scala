package cowsay4s.core

case class CowCommand(
    action: CowAction,
    cow: Cow,
    face: CowFace,
    wrap: StrictPositiveInt,
    message: String
)

object CowCommand {
  def apply(
      action: CowAction,
      cow: Cow,
      mode: CowMode,
      wrap: StrictPositiveInt,
      message: String
  ): CowCommand =
    new CowCommand(action, cow, mode.face, wrap, message)
}
