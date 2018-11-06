package cowsay4s.core

import cowsay4s.core.impl.ThrowableUtil

sealed trait CowError {
  def printError: String
}

object CowError {

  final case class CowNotFound(cowName: String) extends CowError {
    override def printError: String = s"Cow named '$cowName' was not found"
  }

  final case class CowReadingException(t: Throwable) extends CowError {
    override def printError: String =
      s"""An exception occured while reading the cow file:
         |${ThrowableUtil.printStackTrace(t)}""".stripMargin
  }

  final case class CowParsingError(message: String) extends CowError {
    override def printError: String = s"Cow parsing error: $message"
  }
}
