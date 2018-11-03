package cowsay4s.core

import scala.collection.immutable

import enumeratum.{Enum, EnumEntry}

sealed abstract class CowMode(val face: CowFace)
    extends EnumEntry

object CowMode extends Enum[CowMode] {

  case object Default extends CowMode(CowFace("oo", "  "))

  case object Borg extends CowMode(CowFace("==", "  ")) // b
  case object Dead extends CowMode(CowFace("xx", "U ")) // d
  case object Greedy extends CowMode(CowFace("$$", "  ")) // g
  case object Paranoia extends CowMode(CowFace("@@", "  ")) // p
  case object Stoned extends CowMode(CowFace("**", "U ")) // s
  case object Tired extends CowMode(CowFace("--", "  ")) // t
  case object Wired extends CowMode(CowFace("OO", "  ")) // w
  case object Youthful extends CowMode(CowFace("..", "  ")) // y

  override def values: immutable.IndexedSeq[CowMode] = findValues
}
