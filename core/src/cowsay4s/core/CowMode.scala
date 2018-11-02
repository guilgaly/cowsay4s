package cowsay4s.core

import scala.collection.immutable

import enumeratum.{Enum, EnumEntry}

sealed abstract class CowMode(code: String, face: CowFace) extends EnumEntry

object CowMode extends Enum[CowMode] {

  case object Borg extends CowMode("b", CowFace("==", "  "))
  case object Dead extends CowMode("d", CowFace("xx", "U "))
  case object Greedy extends CowMode("g", CowFace("$$", "  "))
  case object Paranoia extends CowMode("p", CowFace("@@", "  "))
  case object Stoned extends CowMode("s", CowFace("**", "U "))
  case object Tired extends CowMode("t", CowFace("--", "  "))
  case object Wired extends CowMode("w", CowFace("OO", "  "))
  case object Youthful extends CowMode("y", CowFace("..", "  "))

  override def values: immutable.IndexedSeq[CowMode] = findValues
}
