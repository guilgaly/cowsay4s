package cowsay4s.core.defaults

import cowsay4s.core._
import enumeratum.EnumEntry

import scala.collection.immutable

sealed abstract class DefaultCowMode(
    val modeName: String,
    _eyes: String,
    _tongue: String
) extends EnumEntry
    with CowMode {
  override val eyes: CowEyes = CowEyes(_eyes)
  override val tongue: CowTongue = CowTongue(_tongue)
}

object DefaultCowMode extends EnumWithDefault[DefaultCowMode] {
  case object Default
      extends DefaultCowMode(
        "default",
        CowEyes.default.value,
        CowTongue.default.value
      )

  case object Borg extends DefaultCowMode("borg", "==", "  ") // b
  case object Dead extends DefaultCowMode("dead", "xx", "U ") // d
  case object Greedy extends DefaultCowMode("greedy", "$$", "  ") // g
  case object Paranoia extends DefaultCowMode("paranoia", "@@", "  ") // p
  case object Stoned extends DefaultCowMode("stoned", "**", "U ") // s
  case object Tired extends DefaultCowMode("tired", "--", "  ") // t
  case object Wired extends DefaultCowMode("wired", "OO", "  ") // w
  case object Youthful extends DefaultCowMode("youthful", "..", "  ") // y

  override def values: immutable.IndexedSeq[DefaultCowMode] = findValues

  override def defaultValue: DefaultCowMode = Default
}
