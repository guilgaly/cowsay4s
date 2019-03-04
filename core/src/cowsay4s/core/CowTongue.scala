package cowsay4s.core

import cowsay4s.core.impl.TextUtil.normalizeToDisplayLength

case class CowTongue private (value: String)

object CowTongue {
  def apply(value: String): CowTongue =
    new CowTongue(normalizeToDisplayLength(value, 2))

  val default: CowTongue = CowTongue("  ")
}
