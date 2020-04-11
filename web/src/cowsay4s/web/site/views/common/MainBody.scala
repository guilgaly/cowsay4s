package cowsay4s.web.site.views.common

import scalatags.Text.all._

object MainBody {

  def render(xs: Modifier*): Frag =
    div(cls := "main-body")(xs)
}
