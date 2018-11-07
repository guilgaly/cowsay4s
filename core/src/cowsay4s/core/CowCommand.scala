package cowsay4s.core

case class CowCommand(
    action: CowAction,
    provider: CowProvider,
    face: CowFace,
    wrap: StrictPositiveInt,
    message: String
)
