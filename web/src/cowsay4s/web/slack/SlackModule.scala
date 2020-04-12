package cowsay4s.web.slack

import akka.actor.ActorSystem
import com.softwaremill.macwire._
import cowsay4s.core.CowSay
import cowsay4s.web.common.db.Database
import cowsay4s.web.slack.persistence.TeamRegistrationDao
import cowsay4s.web.{RouteProvider, ServerSettings}

import scala.concurrent.ExecutionContext

trait SlackModule {
  def settings: ServerSettings
  def database: Database
  def cowSay: CowSay

  implicit def system: ActorSystem
  implicit def ec: ExecutionContext

  lazy val slackRoutes: RouteProvider = new SlackRoutes(
    new SlackCowsayRoutes(settings, slackpiClient, slackCowsay),
    new SlackOauthRoutes(settings, teamRegistrationDao, slackpiClient),
  )

  lazy val teamRegistrationDao: TeamRegistrationDao = wire[TeamRegistrationDao]

  lazy val slackpiClient: SlackApiClient = wire[SlackApiClient]

  lazy val slackCowsay: SlackCowsay = wire[SlackCowsay]
}
