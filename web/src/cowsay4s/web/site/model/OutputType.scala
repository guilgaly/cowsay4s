package cowsay4s.web.site.model

import scala.collection.immutable

import akka.http.scaladsl.unmarshalling.FromStringUnmarshaller
import enumeratum.EnumEntry

import cowsay4s.core.EnumWithDefault
import cowsay4s.web.util.MarshallingUtils

sealed trait OutputType extends EnumEntry

object OutputType extends EnumWithDefault[OutputType] {

  case object Text extends OutputType
  case object Png extends OutputType

  override def values: immutable.IndexedSeq[OutputType] = findValues

  implicit val unmarshaller: FromStringUnmarshaller[OutputType] =
    MarshallingUtils.enumFromStringUnmarshaller(OutputType, "outputType")

  override def defaultValue: OutputType = Text
}
