package cowsay4s.core

sealed trait CowError

object CowError {
  case object CowNotFound extends CowError
  final case class CowReadingException(t: Throwable) extends CowError
  final case class CowParsingError(message: String) extends CowError
}
