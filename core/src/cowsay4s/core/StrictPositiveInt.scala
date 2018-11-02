package cowsay4s.core

case class StrictPositiveInt private (value: Int) extends AnyVal

object StrictPositiveInt {
  def apply(value: Int): StrictPositiveInt = {
    require(value > 0, s"$value is not strictly positive")
    new StrictPositiveInt(value)
  }
}
