package cowsay4s.web.slack.model

import akka.http.scaladsl.common.NameReceptacle
import akka.http.scaladsl.server.Directives._

case class TalkCommand(
    slashCommand: SlashCommand,
    text: String,
    userId: String,
    teamId: String,
    responseUrl: String
)

object TalkCommand {
  val fields: (NameReceptacle[SlashCommand], String, String, String, String) =
    ("command".as[SlashCommand], "text", "user_id", "team_id", "response_url")
}
