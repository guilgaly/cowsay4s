package cowsay4s.web.slack.model

import play.api.libs.json._
import play.api.libs.json.JsonConfiguration.Aux
import play.api.libs.json.JsonNaming.SnakeCase

case class AccessToken(
    accessToken: String,
    scope: String,
    teamName: String,
    teamId: String,
)

object AccessToken {
  implicit val reads: Reads[AccessToken] = {
    implicit val config: Aux[Json.MacroOptions] = JsonConfiguration(SnakeCase)
    Json.reads[AccessToken]
  }
}
