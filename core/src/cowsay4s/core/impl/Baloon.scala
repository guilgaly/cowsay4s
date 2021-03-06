package cowsay4s.core.impl

import cowsay4s.core.{CowAction, MessageWrapping}

private[core] object Baloon {
  final case class DelimiterCouple(start: Char, end: Char)

  trait Delimiters {
    def first: DelimiterCouple
    def middle: DelimiterCouple
    def last: DelimiterCouple
    def only: DelimiterCouple
  }

  object Delimiters {
    def fromAction(action: CowAction): Delimiters = action match {
      case CowAction.CowSay   => Baloon.SayDelimiters
      case CowAction.CowThink => Baloon.ThinkDelimiters
    }
  }

  case object SayDelimiters extends Delimiters {
    override val first: DelimiterCouple = DelimiterCouple('/', '\\')
    override val middle: DelimiterCouple = DelimiterCouple('|', '|')
    override val last: DelimiterCouple = DelimiterCouple('\\', '/')
    override val only: DelimiterCouple = DelimiterCouple('<', '>')
  }

  case object ThinkDelimiters extends Delimiters {
    final private val parensDelims = DelimiterCouple('(', ')')
    override val first: DelimiterCouple = parensDelims
    override val middle: DelimiterCouple = parensDelims
    override val last: DelimiterCouple = parensDelims
    override val only: DelimiterCouple = parensDelims
  }

  def format(
      text: String,
      lineWidth: MessageWrapping,
      delimiters: Delimiters,
  ): String = {
    val lines = TextUtil.softWrap(text, lineWidth.value)
    val maxLength = lines.map(TextUtil.displayLength).max

    val top = topLine(maxLength)
    val middle = lines match {
      case Vector() =>
        Nil
      case Vector(one) =>
        List(line(one, maxLength, delimiters.only))
      case longer =>
        val lastIdx = longer.size - 1
        longer.view.zipWithIndex.map { case (txt, idx) =>
          val delim =
            if (idx == 0) delimiters.first
            else if (idx == lastIdx) delimiters.last
            else delimiters.middle
          line(txt, maxLength, delim)
        }.toList
    }
    val bottom = bottomLine(maxLength)

    (top +: middle :+ bottom).mkString("\n")
  }

  private def line(textLine: String, textLength: Int, delims: DelimiterCouple) =
    s"${delims.start} ${TextUtil.padToDisplayLength(textLine, textLength)} ${delims.end}"

  private def topLine(length: Int) = s" ${"_" * (length + 2)} "

  private def bottomLine(length: Int) = s" ${"-" * (length + 2)} "
}
