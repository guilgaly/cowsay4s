package cowsay4s.web.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.{get, post}
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import cowsay4s.web.api.model.{About, TalkCommand}
import cowsay4s.web.{JsonSupport, RouteProvider}

final class ApiRoutes(apiCowsay: ApiCowsay)
    extends RouteProvider
    with JsonSupport {

  def apply(): Route =
    pathPrefix("api") {
      concat(
        path("about") {
          get {
            complete((StatusCodes.OK, About("v1")))
          }
        },
        path("talk") {
          post {
            entity(as[TalkCommand]) { talkCommand =>
              val talkResponse = apiCowsay.talk(talkCommand)
              complete((StatusCodes.OK, talkResponse))
            }
          }
        },
      )
    }
}
