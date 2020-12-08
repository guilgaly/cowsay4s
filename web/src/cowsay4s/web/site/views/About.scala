package cowsay4s.web.site.views

import scalatags.Text.all._

import cowsay4s.web.site.views.common.Page

object About extends Page {

  val render: Frag =
    renderPage(Some("About Cowsay Online"))(
      p(
        "This is an implementation of cowsay as a webapp, because we all know that of course everything is better in the Cloud. Written in Scala with Akka HTTP.",
      ),
      p(
        "It aims to provide, in a single app:",
        ul(
          li("a webpage to manually generate cowsay outputs"),
          li("a RESTful API providing the same service"),
          li("integration with Slack (using slash commands)"),
        ),
      ),
      p("Props to Tony Monroe for the original cowsay program."),
      p(pre(cowsayIllustration)),
      p(
        "Source code available ",
        a(href := "https://github.com/guilgaly/cowsay4s")("on GitHub"),
        ".",
      ),
      p(
        "See also:",
        ul(
          li(
            a(href := "https://en.wikipedia.org/wiki/Cowsay")("Wikipedia"),
            " for more info on cowsay",
          ),
          li(
            a(href := "https://github.com/tnalpgge/rank-amateur-cowsay")(
              "tnalpgge/rank-amateur-cowsay",
            ),
            " for the original program",
          ),
        ),
      ),
    )

  private def cowsayIllustration =
    """ _____________________________
      |< Cows ♥ Scala and Akka HTTP! >
      | -----------------------------
      |        \   ^__^
      |         \  (oo)\_______
      |            (__)\       )\/\
      |                ||----w |
      |                ||     ||""".stripMargin
}
