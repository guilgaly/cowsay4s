package cowsay4s.web.site.model

import akka.http.scaladsl.unmarshalling.FromStringUnmarshaller
import cowsay4s.core.CowAction
import cowsay4s.core.defaults.{DefaultCow, DefaultCowMode}
import cowsay4s.web.util.MarshallingUtils

case class TalkCommand(
    message: String,
    action: CowAction,
    cow: DefaultCow,
    mode: DefaultCowMode,
)

object TalkCommand {

  def withDefaults(
      message: String,
      action: Option[CowAction],
      defaultCow: Option[DefaultCow],
      mode: Option[DefaultCowMode],
  ): TalkCommand =
    TalkCommand(
      message,
      action.getOrElse(CowAction.defaultValue),
      defaultCow.getOrElse(DefaultCow.defaultValue),
      mode.getOrElse(DefaultCowMode.defaultValue),
    )

  val default: TalkCommand = withDefaults("", None, None, None)

  object Unmarshallers {

    implicit val cowActionUnmarshaller: FromStringUnmarshaller[CowAction] =
      MarshallingUtils.enumFromStringUnmarshaller(CowAction, "action")

    implicit val cowModeUnmarshaller: FromStringUnmarshaller[DefaultCowMode] =
      MarshallingUtils.enumFromStringUnmarshaller(DefaultCowMode, "mode")

    implicit val defaultCowUnmarshaller: FromStringUnmarshaller[DefaultCow] =
      MarshallingUtils.enumFromStringUnmarshaller(DefaultCow, "cow")
  }
}
