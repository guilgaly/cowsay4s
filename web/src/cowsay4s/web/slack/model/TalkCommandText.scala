package cowsay4s.web.slack.model

import cowsay4s.core.defaults.{DefaultCow, DefaultCowMode}
import enumeratum.{Enum, EnumEntry}

import scala.collection.immutable

case class TalkCommandText(
    cow: DefaultCow,
    mode: DefaultCowMode,
    message: String,
)

object TalkCommandText {

  def withDefaults(
      cow: Option[DefaultCow],
      mode: Option[DefaultCowMode],
      message: String,
  ): TalkCommandText =
    new TalkCommandText(
      cow.getOrElse(DefaultCow.defaultValue),
      mode.getOrElse(DefaultCowMode.defaultValue),
      message,
    )

  object Parser {
    import fastparse._
    import NoWhitespace._
    import ParsingError._

    def apply(
        text: String,
    ): Either[List[TalkCommandText.ParsingError], TalkCommandText] =
      parse(text.trim, parser(_)) match {
        case Parsed.Success((options, message), _) =>
          val maybeCow = options.get(CmdOption.Cow) match {
            case Some("random") =>
              Right(DefaultCow.randomValue)
            case Some(cowStr) =>
              DefaultCow
                .withCowNameInsensitive(cowStr)
                .toRight(InvalidCow(cowStr))
            case None =>
              Right(DefaultCow.defaultValue)
          }
          val maybeMode = options.get(CmdOption.Mode) match {
            case Some("random") =>
              Right(DefaultCowMode.randomValue)
            case Some(modeStr) =>
              DefaultCowMode
                .withNameInsensitiveOption(modeStr)
                .toRight(InvalidMode(modeStr))
            case None =>
              Right(DefaultCowMode.defaultValue)
          }
          (maybeCow, maybeMode) match {
            case (Left(err1), Left(err2)) =>
              Left(List(err1, err2))
            case (Left(err), _) =>
              Left(List(err))
            case (_, Left(err)) =>
              Left(List(err))
            case (Right(cow), Right(mode)) =>
              Right(TalkCommandText(cow, mode, message))
          }
        case _: Parsed.Failure =>
          Left(List(ParsingError.InvalidCommandText))
      }

    private def parser[_: P] =
      P(cmdOptions ~ AnyChar.rep.! ~ End)

    private def cmdOptions[_: P] = P(
      (cmdOptionType ~ "=" ~ notWhitespace ~ whitespace.rep(1)).rep
        .map(_.toMap),
    )

    private def cmdOptionType[_: P]: P[CmdOption] = {
      import CmdOption._
      P("cow".!.map(_ => Cow) | "mode".!.map(_ => Mode))
    }

    private def whitespace[_: P] =
      CharsWhile(_.isWhitespace)

    private def notWhitespace[_: P] =
      CharsWhile(!_.isWhitespace).rep.!

    sealed private trait CmdOption
    private object CmdOption {
      case object Cow extends CmdOption
      case object Mode extends CmdOption
    }
  }

  sealed trait ParsingError extends EnumEntry
  object ParsingError extends Enum[ParsingError] {
    object InvalidCommandText extends ParsingError
    final case class InvalidCow(cow: String) extends ParsingError
    final case class InvalidMode(mode: String) extends ParsingError

    override def values: immutable.IndexedSeq[ParsingError] = findValues
  }
}
