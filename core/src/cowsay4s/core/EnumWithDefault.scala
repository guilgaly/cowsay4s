package cowsay4s.core

import enumeratum.{Enum, EnumEntry}

import scala.collection.immutable
import scala.util.Random

trait EnumWithDefault[A <: EnumEntry] extends Enum[A] {

  def defaultValue: A

  final def nonDefaultValues: immutable.IndexedSeq[A] =
    values.filterNot(_ == defaultValue)

  final def randomValue: A =
    values(Random.nextInt(values.size))
}
