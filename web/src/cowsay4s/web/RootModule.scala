package cowsay4s.web

import akka.actor.ActorSystem
import akka.dispatch.MessageDispatcher
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.typesafe.config.{Config, ConfigFactory}
import cowsay4s.core.CowSay
import cowsay4s.web.api.ApiModule
import cowsay4s.web.common.db.Database
import cowsay4s.web.site.SiteModule
import cowsay4s.web.slack.SlackModule

import scala.concurrent.ExecutionContext

trait RootModule extends ApiModule with SiteModule with SlackModule {

  lazy val config: Config = ConfigFactory.load()
  lazy val settings: ServerSettings = new ServerSettings(config)

  implicit lazy val system: ActorSystem =
    ActorSystem("cowsay-online-system", config)
  implicit lazy val ec: ExecutionContext = system.dispatcher

  lazy val databaseEc: MessageDispatcher =
    system.dispatchers.lookup("db-context")
  lazy val database: Database = new Database(settings, databaseEc)

  lazy val cowSay: CowSay = CowSay.default

  lazy val allRoutes: Route =
    (encodeResponse & redirectToNoTrailingSlashIfPresent(StatusCodes.Found)) {
      concat(
        siteRoutes(),
        apiRoutes(),
        slackRoutes(),
      )
    }
}
