package cowsay4s.web.site.views.common

import scalatags.Text.all._

trait Page {

  final protected def renderPage(
      pageTitle: Option[String]
  )(mainBodyContent: Modifier*): Frag =
    html(lang := "en")(
      Head.render((Seq("Cowsay Online") ++ pageTitle).mkString(" | ")),
      body(
        Header.render,
        mainBody(pageTitle, mainBodyContent),
        Footer.render
      )
    )

  private def mainBody(
      pageTitle: Option[String],
      mainBodyContent: Seq[Modifier]
  ) = {
    val h2Title: Frag = pageTitle.map(h2(_))
    MainBody.render(h2Title +: mainBodyContent: _*)
  }
}
