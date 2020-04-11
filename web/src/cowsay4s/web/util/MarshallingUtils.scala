package cowsay4s.web.util

import akka.http.scaladsl.unmarshalling.{FromStringUnmarshaller, Unmarshaller}
import enumeratum.{Enum, EnumEntry}

object MarshallingUtils {

  def enumFromStringUnmarshaller[A <: EnumEntry, E <: Enum[A]](
      enum: E,
      typeName: String
  ): FromStringUnmarshaller[A] =
    Unmarshaller.strict { string =>
      enum
        .withNameInsensitiveOption(string)
        .getOrElse(
          throw new IllegalArgumentException(
            s"$string is not a valid $typeName"
          )
        )
    }
}
