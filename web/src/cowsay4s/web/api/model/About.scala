package cowsay4s.web.api.model

import cowsay4s.web.BuildInfo
import play.api.libs.json.{Json, OFormat}

case class About(
    name: String,
    version: String,
    scalaVersion: String,
    apiVersion: String
)

object About {

  def apply(apiVersion: String): About =
    About(BuildInfo.name, BuildInfo.version, BuildInfo.scalaVersion, apiVersion)

  implicit val format: OFormat[About] = Json.format
}
