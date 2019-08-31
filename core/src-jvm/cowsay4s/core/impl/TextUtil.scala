package cowsay4s.core.impl

import java.text.BreakIterator

private[core] object TextUtil {

  // http://tutorials.jenkov.com/java-internationalization/breakiterator.html

  def softWrap(text: String, lineWidth: Int): List[String] =
    if (text.isEmpty) List(text)
    else text.linesIterator.flatMap(softWrapLine(_, lineWidth)).toList

  private def softWrapLine(text: String, lineWidth: Int): List[String] = {
    val breakIterator = BreakIterator.getLineInstance
    breakIterator.setText(text)

    var start = breakIterator.first
    var end = breakIterator.next()
    var lines = List.empty[String]
    var currentLine = new StringBuilder(lineWidth)
    var currentLineLength = 0

    while (end != BreakIterator.DONE) {
      val word = text.substring(start, end)
      val wordLength = displayLength(word)
      if (currentLineLength == 0 || (currentLineLength + wordLength) <= lineWidth) {
        currentLine.append(word)
        currentLineLength += wordLength
      } else {
        val trimmedWord = word.replaceAll("""(?m)\s+$""", "")
        val trimmedWordLength = displayLength(trimmedWord)
        if ((currentLineLength + trimmedWordLength) <= lineWidth) {
          currentLine.append(trimmedWord)
          lines = currentLine.mkString +: lines
          currentLine = new StringBuilder(lineWidth)
          currentLineLength = 0
        } else {
          lines = currentLine.mkString.replaceAll("""(?m)\s+$""", "") +: lines
          currentLine = new StringBuilder(lineWidth).append(word)
          currentLineLength = wordLength
        }
      }
      start = end
      end = breakIterator.next()
    }
    lines = currentLine.mkString.replaceAll("""(?m)\s+$""", "") +: lines

    lines.reverse
  }

  def displayLength(text: String): Int = {
    val breakIterator = BreakIterator.getCharacterInstance
    breakIterator.setText(text)

    var length = 0
    while (breakIterator.next() != BreakIterator.DONE) {
      length += 1
    }
    length
  }

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

  private def doCut(text: String, targetLength: Int) = {
    val breakIterator = BreakIterator.getCharacterInstance
    breakIterator.setText(text)

    for (_ <- 1 to targetLength) {
      breakIterator.next()
    }
    text.substring(0, breakIterator.current())
  }
}
