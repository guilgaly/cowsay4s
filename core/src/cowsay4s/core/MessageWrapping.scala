package cowsay4s.core

case class MessageWrapping private (value: Int) extends AnyVal

object MessageWrapping {
  def apply(value: Int): MessageWrapping = {
    require(
      value > 0,
      s"$value is not a valid MessageWrapping value; it should be strictly positive"
    )
    new MessageWrapping(value)
  }

  val default = MessageWrapping(40)
}
