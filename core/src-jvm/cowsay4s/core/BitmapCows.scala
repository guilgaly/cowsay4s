package cowsay4s.core

import java.awt.RenderingHints._
import java.awt.image.BufferedImage
import java.awt.{Color, Font}
import java.io.ByteArrayOutputStream
import java.nio.file.Path

import javax.imageio.ImageIO

object BitmapCows {

  implicit final class CowSayToPng(cowSay: CowSay) {

    def talkToBufferedImage(
        command: CowCommand,
        font: Font,
        fontColor: Color,
        backgroundColor: Option[Color] = None): BufferedImage = {
      val text = cowSay.talk(command)
      val lines = text.lines.toList

      renderImage(lines, font, fontColor, backgroundColor)
    }

    def talkToPng(
        command: CowCommand,
        font: Font,
        fontColor: Color,
        backgroundColor: Option[Color] = None): Array[Byte] = {
      val img = talkToBufferedImage(command, font, fontColor, backgroundColor)
      writeToByteArray(img, "png")
    }

    def talkToPngFile(
        path: Path,
        command: CowCommand,
        font: Font,
        fontColor: Color,
        backgroundColor: Option[Color] = None): Unit = {
      val img = talkToBufferedImage(command, font, fontColor, backgroundColor)
      ImageIO.write(img, "png", path.toFile)
      ()
    }

    private def renderImage(
        lines: List[String],
        font: Font,
        fontColor: Color,
        backgroundColor: Option[Color]) = {
      val (width, height) = textSize(lines, font)

      val img = createImage(width, height)
      val g2d = setupGraphics2D(img, font, fontColor, backgroundColor)
      val fontMetrics = g2d.getFontMetrics()
      lines.zipWithIndex.foreach {
        case (line, idx) =>
          val y = fontMetrics.getAscent + fontMetrics.getHeight * idx
          g2d.drawString(line, 0, y)
      }
      g2d.dispose()
      img
    }

    private def setupGraphics2D(
        img: BufferedImage,
        font: Font,
        fontColor: Color,
        backgroundColor: Option[Color]) = {
      val g2d = img.createGraphics()

      backgroundColor.foreach { color =>
        g2d.setColor(color)
        g2d.fillRect(0, 0, img.getWidth, img.getHeight)
      }

      g2d.setColor(fontColor)
      g2d.setFont(font)
      g2d.setRenderingHint(
        KEY_ALPHA_INTERPOLATION,
        VALUE_ALPHA_INTERPOLATION_QUALITY)
      g2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON)
      g2d.setRenderingHint(KEY_COLOR_RENDERING, VALUE_COLOR_RENDER_QUALITY)
      g2d.setRenderingHint(KEY_DITHERING, VALUE_DITHER_ENABLE)
      g2d.setRenderingHint(KEY_FRACTIONALMETRICS, VALUE_FRACTIONALMETRICS_ON)
      g2d.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BILINEAR)
      g2d.setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY)
      g2d.setRenderingHint(KEY_STROKE_CONTROL, VALUE_STROKE_PURE)
      g2d
    }

    private def textSize(lines: List[String], font: Font) = {
      // Because font metrics is based on a graphics context, we need to create a
      // small, temporary image so we can ascertain the width and height of the
      // final image
      val img = createImage(1, 1)
      val g2d = img.createGraphics()
      g2d.setFont(font)
      val fontMetrics = g2d.getFontMetrics()
      val width = lines.map(fontMetrics.stringWidth).max
      val lineHeight = fontMetrics.getHeight
      g2d.dispose()

      val linesCount = lines.size
      (width, linesCount * lineHeight)
    }

    private def createImage(width: Int, height: Int) =
      new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
  }

  private def writeToByteArray(img: BufferedImage, formatName: String) = {
    val output = new ByteArrayOutputStream()
    ImageIO.write(img, formatName, output)
    output.toByteArray
  }
}
