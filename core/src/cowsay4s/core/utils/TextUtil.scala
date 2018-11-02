package cowsay4s.core.utils

import java.util.StringTokenizer

private[core] object TextUtil {

  def wrap(text: String, lineWidth: Int): List[String] = {
    val targetLineWidth = if (lineWidth > 0) lineWidth else 1
    val tokenizer = new StringTokenizer(text)

    var lines = List.empty[String]
    var currentLine = ""
    var spaceLeft = lineWidth
    while (tokenizer.hasMoreTokens) {
      val word = tokenizer.nextToken
      if ((word.length + 1) > spaceLeft) {
        lines = currentLine :: lines
        currentLine = word
        spaceLeft = targetLineWidth - word.length
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
}
