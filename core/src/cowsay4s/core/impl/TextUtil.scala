package cowsay4s.core.impl
import java.util.StringTokenizer

import cowsay4s.core.StrictPositiveInt

private[core] object TextUtil {

  def wrap(text: String, lineWidth: StrictPositiveInt): List[String] = {
    val tokenizer = new StringTokenizer(text)

    var lines = List.empty[String]
    var currentLine = ""
    var spaceLeft = lineWidth.value
    while (tokenizer.hasMoreTokens) {
      val word = tokenizer.nextToken
      if ((word.length + 1) > spaceLeft) {
        lines = currentLine :: lines
        currentLine = word
        spaceLeft = lineWidth.value - word.length
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

  def normalizeLength(text: String, length: Int): String = {
    assert(length > 0)
    if (text.length >= length) text.take(length)
    else text.padTo(length, ' ')
  }
}
