package cowsay4s.web.slack

import java.net.URLEncoder
import java.util.UUID

import scala.concurrent.{ExecutionContext, Future}

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.apache.commons.codec.binary.Base64

import cowsay4s.web.{RouteProvider, ServerSettings}
import cowsay4s.web.slack.persistence.{NewTeamRegistration, TeamRegistrationDao}
import cowsay4s.web.util.SignatureUtils

class SlackOauthRoutes(
    settings: ServerSettings,
    teamRegistrationDao: TeamRegistrationDao,
    slackpiClient: SlackApiClient,
)(implicit system: ActorSystem)
    extends RouteProvider {
  implicit private val ec: ExecutionContext = system.dispatcher

  private val encodedOAuthScopes = Seq("commands").mkString("%20")

  def apply(): Route = pathPrefix("oauth") {
    concat(postSignIn, getAccess)
  }

  private def postSignIn = (path("signin-url") & post) {
    complete {
      generateSignInState().map { state =>
        val redirect =
          URLEncoder.encode(s"${settings.baseUrl}/slack/oauth/access", "UTF-8")
        s"https://slack.com/oauth/authorize?client_id=${settings.slack.clientId}&scope=$encodedOAuthScopes&redirect_uri=$redirect&state=$state"
      }
    }
  }

  private def getAccess = (path("access") & get & parameters("code")) { code =>
    complete {
      val selfUri = s"${settings.baseUrl}/slack/oauth/access"
      for {
        token <- slackpiClient.oauthAccess(code, selfUri)

        newRegistration = NewTeamRegistration(
          token.teamId,
          token.teamName,
          token.accessToken,
          token.scope,
        )
        _ <- teamRegistrationDao.insertOrUpdate(newRegistration)

      } yield HttpResponse(
        status = StatusCodes.SeeOther,
        headers = headers.Location("/") :: Nil,
        entity = HttpEntity.Empty,
      )
    }
  }

  /*
   * Returns string representation, with the first 36 characters
   * as cleartext UUID, following by its signature
   * (HmacSHA256, encoded as Base64).
   */
  private def generateSignInState(): Future[String] = {
    val state = UUID.randomUUID().toString
    val stateSignature = appSignature(state)

    Future.fromTry(stateSignature.map(s => s"$state$s"))
  }

  private def appSignature(stringToSign: String) =
    SignatureUtils
      .signHmacSHA256(stringToSign, settings.secret)
      .map(Base64.encodeBase64URLSafeString)

//  /**
//   * @see [[generateSignInState()]]
//   */
//  private def verifiedSignInState(signedState: String): Future[String] =
//    Future(signedState.splitAt(36)).flatMap {
//      case (state, base63Signature) =>
//        Future.fromTry(
//          appSignature(state).collect {
//            case `base63Signature` => state
//          },
//        )
//    }
}
