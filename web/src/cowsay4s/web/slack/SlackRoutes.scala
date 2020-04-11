package cowsay4s.web.slack

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cowsay4s.web.RouteProvider

final class SlackRoutes(
    cowsayRoutes: SlackCowsayRoutes,
    oauthRoutes: SlackOauthRoutes,
) extends RouteProvider {

  def apply(): Route =
    pathPrefix("slack") {
      concat(
        cowsayRoutes(),
        oauthRoutes(),
      )
    }
}
