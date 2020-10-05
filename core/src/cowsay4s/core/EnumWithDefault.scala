package cowsay4s.core

import scala.collection.immutable
import scala.util.Random

import enumeratum.{Enum, EnumEntry}

trait EnumWithDefault[A <: EnumEntry] extends Enum[A] {
  def defaultValue: A

  final def nonDefaultValues: immutable.IndexedSeq[A] =
    values.filterNot(_ == defaultValue)

  final def randomValue: A =
    values(Random.nextInt(values.size))
}
