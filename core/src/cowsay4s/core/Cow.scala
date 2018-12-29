package cowsay4s.core

import cowsay4s.core.cows.DefaultCowContent
import enumeratum.{Enum, EnumEntry}
import scala.collection.immutable

trait Cow {
  def cowValue: String
}

final case class CustomCow(cowValue: String) extends Cow

sealed abstract class DefaultCow(content: DefaultCowContent)
    extends EnumEntry
    with Cow {
  def cowName: String = content.cowName
  override def cowValue: String = content.cowValue
}

object DefaultCow extends Enum[DefaultCow] {

  case object BeavisZen extends DefaultCow(cows.BeavisZen)
  case object Bong extends DefaultCow(cows.Bong)
  case object Default extends DefaultCow(cows.Default)

  override def values: immutable.IndexedSeq[DefaultCow] = findValues

  def cowNames: immutable.IndexedSeq[String] = values.map(_.cowName)

  def withCowName(cowName: String): Option[DefaultCow] =
    values.find(_.cowName == cowName)
}
