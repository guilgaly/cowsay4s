package cowsay4s.core
import cowsay4s.core.utils.TextUtil

object Baloon {

  final case class DelimiterCouple(start: Char, end: Char)

  trait Delimiters {
    def first: DelimiterCouple
    def middle: DelimiterCouple
    def last: DelimiterCouple
    def only: DelimiterCouple
  }

  case object SayDelimiters extends Delimiters {
    override val first: DelimiterCouple = DelimiterCouple('/', '\\')
    override val middle: DelimiterCouple = DelimiterCouple('|', '|')
    override val last: DelimiterCouple = DelimiterCouple('\\', '/')
    override val only: DelimiterCouple = DelimiterCouple('<', '>')
  }

  case object ThinkDelimiters extends Delimiters {
    private final val parensDelims = DelimiterCouple('(', ')')
    override val first: DelimiterCouple = parensDelims
    override val middle: DelimiterCouple = parensDelims
    override val last: DelimiterCouple = parensDelims
    override val only: DelimiterCouple = parensDelims
  }

  def format(text: String, lineWidth: Int, delimiters: Delimiters): String = {
    val lines = TextUtil.wrap(text, lineWidth)
    val maxLength = lines.map(_.length).max

    val top = topLine(maxLength)
    val middle = lines match {
      case Nil =>
        Nil
      case one +: Nil =>
        List(line(one, maxLength, delimiters.only))
      case head +: mid :+ last =>
        line(head, maxLength, delimiters.first) +:
        mid.map(s => line(s, maxLength, delimiters.middle)) :+
        line(last, maxLength, delimiters.last)
    }
    val bottom = bottomLine(maxLength)

    (top +: middle :+ bottom).mkString("\n")
  }

  private def line(textLine: String, textLength: Int, delims: DelimiterCouple) =
    s"${delims.start} ${textLine.padTo(textLength, ' ')} ${delims.end}"

  private def topLine(length: Int) = s" ${"_" * (length + 2)} "

  private def bottomLine(length: Int) = s" ${"-" * (length + 2)} "
}
