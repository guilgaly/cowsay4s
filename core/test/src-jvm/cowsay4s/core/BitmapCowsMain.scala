package cowsay4s.core

import java.awt.{Color, Font}
import java.nio.file.Paths

object BitmapCowsMain {
  import cowsay4s.core.BitmapCows._

  private val font = new Font("VT323", Font.PLAIN, 18)
  private val message =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
  private val command = CowCommand(TestCow, message, wrap = MessageWrapping(20))
  private val cowsay = CowSay.default

  def main(args: Array[String]): Unit = {
    for (x <- 1 to 10) {
      writeImage(x)
    }
  }

  private def writeImage(idx: Int): Unit = {
    val path = Paths
      .get(System.getProperty("user.home"))
      .resolve(s"Desktop/cowsay-$idx.png")
    println(s"Path: $path")

    val start = System.nanoTime()
    cowsay.talkToPngFile(path, command, font, Color.BLACK, Some(Color.WHITE))
    val end = System.nanoTime()
    val durationMillis = (end - start) / (1000 * 1000)
    println(s"Image rendered in $durationMillis ms")
  }
}
