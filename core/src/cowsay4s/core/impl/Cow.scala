package cowsay4s.core.impl

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import scala.util.Try

import cowsay4s.core.CowError.{
  CowNotFound,
  CowParsingError,
  CowReadingException
}
import cowsay4s.core.{CowError, CowName, CowProvider, CowString}

private[core] case class Cow(value: String) extends AnyVal

private[core] object Cow {

  def load(cowProvider: CowProvider): Either[CowError, Cow] =
    cowProvider match {
      case cowName: CowName     => loadFromClasspath(cowName)
      case cowString: CowString => loadFromString(cowString)
    }

  private def loadFromClasspath(cowName: CowName) =
    for {
      url <- Option {
        val resourceName = s"/cowsay4s/core/${cowName.name}.cow"
        getClass.getResource(resourceName)
      }.toRight(CowNotFound)

      path <- tryCowReading(Paths.get(url.toURI))

      cowString <- tryCowReading {
        new String(Files.readAllBytes(path), StandardCharsets.UTF_8)
      }

      cow <- loadFromString(CowString(cowString))

    } yield cow

  private def loadFromString(cowString: CowString) = {
    val pattern = """(?s).*\$the_cow\s*=\s*<<"?EOC"?;(.*)EOC.*""".r
    val str = cowString.value.replace("\r\n", "\n").replace('\r', '\n')

    str match {
      case pattern(cowValue) => Right(Cow(cowValue))
      case _                 => Left(CowParsingError("Unable to parse cow"))
    }
  }

  private def tryCowReading[T](f: => T) =
    Try(f).toEither.left.map(CowReadingException)
}
