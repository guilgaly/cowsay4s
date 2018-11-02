package cowsay4s.core

sealed trait CowProvider

final case class CowName(name: String) extends CowProvider

final case class CowString(value: String) extends CowProvider
