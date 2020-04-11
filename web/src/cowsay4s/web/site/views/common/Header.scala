package cowsay4s.web.site.views.common

import scalatags.Text.all._
import scalatags.Text.tags2

object Header {

  val render: Frag =
    header(
      h1(a(href := "/")("Cowsay Online")),
      tags2.nav(
        ul(
          li(a(href := "/")("Home")),
          li(a(href := "/about")("About")),
          li(a(href := "/cowsay4slack")("Slack integration")),
          li(a(href := "/listCows")("List cows")),
        ),
      ),
    )
}
