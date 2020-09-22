package cowsay4s.web.slack

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshaller}
import cowsay4s.web.slack.model.{SlashCommand, TalkCommand}
import cowsay4s.web.util.SignatureUtils
import cowsay4s.web.{JsonSupport, RouteProvider, ServerSettings}
import org.apache.commons.codec.binary.Hex

import scala.concurrent.ExecutionContext

class SlackCowsayRoutes(
    settings: ServerSettings,
    slackApiClient: SlackApiClient,
    slackCowsay: SlackCowsay,
)(implicit ec: ExecutionContext)
    extends RouteProvider
    with JsonSupport {

  def apply(): Route = (path("talk") & post) {
    (validateSignature & ignoreSslChecks) {
      formFields(
        "command".as[SlashCommand],
        "text",
        "user_id",
        "team_id",
        "response_url",
      ).as(TalkCommand.apply _) { command =>
        slackCowsay.talk(command).flatMap { response =>
          slackApiClient.respondToSlashCommand(command.responseUrl, response)
        }
        val ackResponse = s"`${command.slashCommand.command} ${command.text}`"
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, ackResponse))
      }
    }
  }

  private def validateSignature = {
    def extractValues = {
      implicit val strUnmarshaller: FromEntityUnmarshaller[String] =
        Unmarshaller.stringUnmarshaller

      headerValueByName("X-Slack-Request-Timestamp") &
        headerValueByName("X-Slack-Signature") &
        entity(as[String])
    }

    extractValues.trequire { case (timestamp, expectedSignature, body) =>
      val stringToSign = s"v0:$timestamp:$body"
      SignatureUtils
        .signHmacSHA256(stringToSign, settings.slack.signingSecret)
        .map { signature =>
          s"v0=${Hex.encodeHexString(signature)}" == expectedSignature
        }
        .getOrElse(false)
    }
  }

  private def ignoreSslChecks =
    formField("ssl_check".as[Int].?)
      .require(!_.contains(1))
      .recover { _ =>
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "OK"))
          .toDirective[Unit]
      }
}
