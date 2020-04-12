package cowsay4s.web.slack

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import cowsay4s.web.slack.model.AccessToken
import cowsay4s.web.{JsonSupport, ServerSettings}
import org.log4s._
import play.api.libs.json.{Json, Writes}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

final class SlackApiClient(settings: ServerSettings)(
    implicit
    system: ActorSystem,
    ec: ExecutionContext,
) extends JsonSupport {
  private[this] val log = getLogger

  def oauthAccess(code: String, redirectUri: String): Future[AccessToken] = {
    val request =
      HttpRequest(
        uri = "https://slack.com/api/oauth.access",
        method = HttpMethods.POST,
        entity = FormData(
          "client_id" -> settings.slack.clientId,
          "client_secret" -> settings.slack.clientSecret,
          "code" -> code,
          "redirect_uri" -> redirectUri,
        ).toEntity,
      )
    Http().singleRequest(request).flatMap { response =>
      Unmarshal(response).to[AccessToken]
    }
  }

  def respondToSlashCommand[R: Writes](
      responseUrl: String,
      response: R,
  ): Future[Unit] = {
    val jsonResponse = Json.toBytes(Json.toJson(response))
    val request =
      HttpRequest(
        uri = responseUrl,
        method = HttpMethods.POST,
        entity = HttpEntity(ContentTypes.`application/json`, jsonResponse),
      )

    def errMsg = "Failed to send slash command response to Slack"

    val f = Http().singleRequest(request)
    f.onComplete {
      case Success(r) if r.status.isSuccess() =>
        ()
      case Success(r) =>
        log.error(s"$errMsg; got response: $r")
      case Failure(e) =>
        log.error(e)(errMsg)
    }
    f.map(_ => ())
  }
}
