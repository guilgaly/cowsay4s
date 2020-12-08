package cowsay4s.web.site.views.common

import scalatags.Text.all._

import cowsay4s.web.BuildInfo

object Footer {

  val render: Frag =
    footer(
      p(
        s"Cowsay Online v${BuildInfo.version} created by Guillaume Galy - ",
        a(href := "https://github.com/guilgaly/cowsay-online")(
          "find out more on Github",
        ),
      ),
    )
}
