package cowsay4s.web

import java.awt.Font

object Fonts {

  lazy val vt323Regular: Font = loadFont("VT323-Regular.ttf")

  private def loadFont(fileName: String) = {
    val is = getClass.getResourceAsStream(s"fonts/$fileName")
    try {
      Font.createFont(Font.TRUETYPE_FONT, is)
    } finally {
      is.close()
    }
  }
}
