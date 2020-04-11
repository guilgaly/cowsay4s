package cowsay4s.web.api.model

import play.api.libs.json.{Json, OFormat}

case class TalkResponse(theCowSaid: String)

object TalkResponse {
  implicit val format: OFormat[TalkResponse] = Json.format
}
