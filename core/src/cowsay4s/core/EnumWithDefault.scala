package cowsay4s.core

import scala.collection.immutable

import enumeratum.{Enum, EnumEntry}

trait EnumWithDefault[A <: EnumEntry] extends Enum[A] {

  def defaultValue: A

  final def nonDefaultValues: immutable.IndexedSeq[A] =
    values.filterNot(_ == defaultValue)
}
