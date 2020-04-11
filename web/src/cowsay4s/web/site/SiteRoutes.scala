package cowsay4s.web.site

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import cowsay4s.core._
import cowsay4s.core.defaults.{DefaultCow, DefaultCowMode}
import cowsay4s.web.site.model.TalkCommand.Unmarshallers._
import cowsay4s.web.site.model.{OutputType, TalkCommand}
import cowsay4s.web.site.views.{About, Cowsay4slack, Home, ListCows}
import cowsay4s.web.{RouteProvider, ServerSettings}
import scalatags.Text.all.Frag

final class SiteRoutes(settings: ServerSettings, siteCowsay: SiteCowsay)
    extends RouteProvider {

  def apply(): Route =
    concat(
      getStaticAssets,
      pathSingleSlash {
        concat(getHome, postHome)
      },
      getAbout,
      getCowsay4slack,
      getListCows
    )

  private def getStaticAssets = pathPrefix("static") {
    getFromResourceDirectory("cowsay4s/web/site/static")
  }

  private def getHome = get {
    completeHtml(Home.renderWithoutCow)
  }

  private def postHome = post {
    formFields(
      (
        "message",
        "action".as[CowAction].?,
        "default-cow".as[DefaultCow].?,
        "mode".as[DefaultCowMode].?,
        "outputType".as[OutputType].?
      )
    ) { (message, cowAction, defaultCow, cowMode, outputType) =>
      val talkCommand =
        TalkCommand.withDefaults(message, cowAction, defaultCow, cowMode)

      outputType.getOrElse(OutputType.defaultValue) match {
        case OutputType.Text =>
          val cow = siteCowsay.talkToText(talkCommand)
          completeHtml(Home.renderWithTextCow(cow, talkCommand))
        case OutputType.Png =>
          val cow = siteCowsay.talkToPng(talkCommand)
          completeHtml(Home.renderWithPngCow(cow, talkCommand))
      }

    }
  }

  private def getAbout = (path("about") & get) {
    completeHtml(About.render)
  }

  private def getCowsay4slack = (path("cowsay4slack") & get) {
    completeHtml(Cowsay4slack.render(settings))
  }

  private def getListCows = (path("listCows") & get) {
    completeHtml(ListCows.render)
  }

  private def completeHtml(html: Frag) =
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, html.render))
}
