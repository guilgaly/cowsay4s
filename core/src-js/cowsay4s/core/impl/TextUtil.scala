package cowsay4s.core.impl

private[core] object TextUtil {
  // java.text.BreakIterator isn't supported on Scala.js

  def softWrap(text: String, lineWidth: Int): Vector[String] =
    if (text.isEmpty) Vector(text)
    else text.linesIterator.flatMap(softWrapLine(_, lineWidth)).toVector

  private def softWrapLine(text: String, lineWidth: Int): List[String] = {
    var lines = List.empty[String]
    var currentLine = ""
    var spaceLeft = lineWidth

    val tokens = text.split("""\s""")
    for (word <- tokens) {
      if ((word.length + 1) > spaceLeft) {
        lines = currentLine :: lines
        currentLine = word
        spaceLeft = lineWidth - word.length
      } else {
        currentLine = if (currentLine.isEmpty) word else s"$currentLine $word"
        spaceLeft = spaceLeft - (word.length + 1)
      }
    }
    if (lines.isEmpty || currentLine.nonEmpty) {
      lines = currentLine :: lines
    }
    lines.reverse
  }

  def displayLength(text: String): Int =
    text.length

  def padToDisplayLength(text: String, targetLength: Int): String = {
    val currentLength = displayLength(text)
    if (currentLength >= targetLength)
      text
    else
      doPad(text, currentLength, targetLength)
  }

  def normalizeToDisplayLength(text: String, targetLength: Int): String = {
    val currentLength = displayLength(text)
    if (currentLength > targetLength) {
      doCut(text, targetLength)
    } else if (currentLength < targetLength) {
      doPad(text, currentLength, targetLength)
    } else {
      text
    }
  }

  private def doPad(text: String, currentLength: Int, targetLength: Int) = {
    val padding = " " * (targetLength - currentLength)
    text + padding
  }

  private def doCut(text: String, targetLength: Int) =
    text.substring(0, targetLength)
}
