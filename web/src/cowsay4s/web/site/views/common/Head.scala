package cowsay4s.web.site.views.common

import scalatags.Text.all._
import scalatags.Text.tags2

object Head {

  private val metaCharset =
    meta(charset := "utf-8")
  private val metaDescription =
    meta(name := "description", content := "Cowsay Online")
  private val linkFont = link(
    rel := "stylesheet",
    href := "https://fonts.googleapis.com/css?family=VT323"
  )
  private val linkStylesheet =
    link(rel := "stylesheet", href := "/static/styles.css")

  def render(pageTitle: String) =
    head(
      metaCharset,
      tags2.title(pageTitle),
      metaDescription,
      linkFont,
      linkStylesheet
    )
}
