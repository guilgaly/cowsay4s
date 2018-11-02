package cowsay4s.core.impl

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import scala.util.Try

import cowsay4s.core.CowError
import cowsay4s.core.CowError.{
  CowNotFound,
  CowParsingError,
  CowReadingException
}

private[core] case class Cow(value: String) extends AnyVal

private[core] object Cow {

  def loadFromClasspath(cowName: String): Either[CowError, Cow] =
    for {
      url <- Option {
        val resourceName = s"/cowsay4s/core/$cowName.cow"
        println(resourceName)
        getClass.getResource(resourceName)
      }.toRight(CowNotFound)

      path <- tryCowReading(Paths.get(url.toURI))

      cowFileContent <- tryCowReading {
        new String(Files.readAllBytes(path), StandardCharsets.UTF_8)
      }

      cow <- loadFromString(cowFileContent)

    } yield cow

  def loadFromString(str: String): Either[CowError, Cow] = {
    val pattern = """(?s).*\$the_cow\s*=\s*<<"?EOC"?;(.*)EOC.*""".r
    val normalizedString = str.replace("\r\n", "\n").replace('\r', '\n')

    normalizedString match {
      case pattern(cowValue) => Right(Cow(cowValue))
      case _                 => Left(CowParsingError("Unable to parse cow"))
    }
  }

  private def tryCowReading[T](f: => T): Either[CowError, T] =
    Try(f).toEither.left.map(CowReadingException)
}
