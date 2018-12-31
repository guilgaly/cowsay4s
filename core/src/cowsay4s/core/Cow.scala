package cowsay4s.core

trait Cow {
  def cowValue: String
}

final case class CustomCow(cowValue: String) extends Cow
