package cowsay4s.core

import scala.collection.immutable

import enumeratum.{Enum, EnumEntry}

sealed abstract class CowAction(val verb: String) extends EnumEntry

object CowAction extends Enum[CowAction] {

  case object CowSay extends CowAction("say")
  case object CowThink extends CowAction("think")

  override def values: immutable.IndexedSeq[CowAction] = findValues
}
