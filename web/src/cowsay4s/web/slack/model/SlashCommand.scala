package cowsay4s.web.slack.model

import akka.http.scaladsl.unmarshalling.{FromStringUnmarshaller, Unmarshaller}
import cowsay4s.core.CowAction
import cowsay4s.web.util.MarshallingUtils
import enumeratum.{Enum, EnumEntry}

import scala.collection.immutable

sealed abstract class SlashCommand(
    val command: String,
    val cowAction: CowAction
) extends EnumEntry {
  override def entryName: String = command
}

object SlashCommand extends Enum[SlashCommand] {

  object CowSay extends SlashCommand("/cowsay", CowAction.CowSay)
  object CowThink extends SlashCommand("/cowthink", CowAction.CowThink)

  override def values: immutable.IndexedSeq[SlashCommand] = findValues

  implicit val unmarshaller: FromStringUnmarshaller[SlashCommand] =
    Unmarshaller.strict[String, String] { str =>
      if (str.endsWith("_local")) str.dropRight(6) else str
    } andThen
      MarshallingUtils.enumFromStringUnmarshaller(this, "slash command")
}
