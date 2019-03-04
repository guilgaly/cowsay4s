package cowsay4s.core

import cowsay4s.core.impl.TextUtil.normalizeToDisplayLength

case class CowEyes private (value: String)

object CowEyes {
  def apply(value: String): CowEyes =
    new CowEyes(normalizeToDisplayLength(value, 2))

  val default: CowEyes = CowEyes("oo")
}
