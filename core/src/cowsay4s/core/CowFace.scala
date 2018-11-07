package cowsay4s.core

import cowsay4s.core.impl.TextUtil.normalizeToDisplayLength

case class CowFace(eyes: CowFace.Eyes, tongue: CowFace.Tongue)

object CowFace {

  def apply(eyes: String, tongue: String): CowFace =
    new CowFace(Eyes(eyes), Tongue(tongue))

  case class Eyes private (value: String)

  object Eyes {
    def apply(value: String): Eyes =
      new Eyes(normalizeToDisplayLength(value, 2))
  }

  case class Tongue private (value: String)

  object Tongue {
    def apply(value: String): Tongue =
      new Tongue(normalizeToDisplayLength(value, 2))
  }
}
