package cowsay4s.web.site.views

import java.nio.charset.StandardCharsets
import java.util.Base64

import enumeratum.EnumEntry
import scalatags.Text.all._
import scalatags.Text.tags2

import cowsay4s.core.{CowAction, EnumWithDefault}
import cowsay4s.core.defaults.{DefaultCow, DefaultCowMode}
import cowsay4s.web.site.model.{OutputType, TalkCommand}
import cowsay4s.web.site.views.common._

object Home extends Page {

  val renderWithoutCow: Frag =
    renderPage(None)(
      cowFormSection(TalkCommand.default),
    )

  def renderWithTextCow(cow: String, talkCommand: TalkCommand): Frag =
    renderPage(None)(
      tags2.section(
        pre(cls := "cow-display")(cow),
      ),
      cowFormSection(talkCommand, OutputType.Text),
    )

  def renderWithPngCow(cow: Array[Byte], talkCommand: TalkCommand): Frag = {
    val base64Data =
      new String(Base64.getEncoder.encode(cow), StandardCharsets.UTF_8)
    val imgSrc = s"data:image/png;base64, $base64Data"
    renderPage(None)(
      tags2.section(
        img(src := imgSrc, cls := "cow-display"),
      ),
      cowFormSection(talkCommand, OutputType.Png),
    )
  }

  private def cowFormSection(
      talkCommand: TalkCommand,
      outputType: OutputType = OutputType.defaultValue,
  ) = {
    tags2.section(
      form(id := "cowform", action := "", method := "post")(
        cowFormActionField(talkCommand.action),
        cowFormMessageField(talkCommand.message),
        cowFormCowField(talkCommand.cow),
        cowFormModeField(talkCommand.mode),
        cowFormOutputTypeField(outputType),
        div(cls := "form-submit-field")(
          input(
            tpe := "submit",
            value := "Make the cow talk",
            cls := "form-button",
          ),
        ),
      ),
    )
  }

  private def cowFormActionField(selected: CowAction) =
    cowFormField("cowform-fieldset-action", "Action:")(
      fieldset(id := "cowform-fieldset-action")(
        input(
          tpe := "radio",
          name := "action",
          value := "CowSay",
          if (selected == CowAction.CowSay) checked := "true" else (),
        )("Say"),
        input(
          tpe := "radio",
          name := "action",
          value := "CowThink",
          if (selected == CowAction.CowThink) checked := "true" else (),
        )("Think"),
      ),
    )

  private def cowFormMessageField(message: String) =
    cowFormField("cowform-input-message", "Message:")(
      textarea(
        id := "cowform-input-message",
        name := "message",
        autofocus := "autofocus",
        cols := "40",
        rows := "5",
        maxlength := "2000",
      )(message),
    )

  private def cowFormCowField(selected: DefaultCow) =
    cowFormField("cowform-select-default-cow", "Cow:")(
      select(id := "cowform-select-default-cow", name := "default-cow")(
        enumOptions(DefaultCow, selected),
      ),
    )

  private def cowFormModeField(selected: DefaultCowMode) =
    cowFormField("cowform-select-mode", "Mode:")(
      select(id := "cowform-select-mode", name := "mode")(
        enumOptions(DefaultCowMode, selected),
      ),
    )

  private def cowFormOutputTypeField(selected: OutputType) =
    cowFormField("cowform-select-outputType", "Output type:")(
      select(id := "cowform-select-outputType", name := "outputType")(
        enumOptions(OutputType, selected),
      ),
    )

  private def cowFormField(id: String, labelText: String)(content: Frag) =
    div(cls := "form-field")(label(attr("for") := id)(labelText), content)

  private def enumOptions[A <: EnumEntry, E <: EnumWithDefault[A]](
      enum: E,
      selectedValue: A,
  ): Frag = {
    val orderedNonDefaults = enum.nonDefaultValues.sortBy(_.entryName)
    val orderedValues = enum.defaultValue +: orderedNonDefaults
    orderedValues.map { entry =>
      option(
        value := entry.entryName,
        if (entry == selectedValue) selected := "true" else "",
      )(entry.entryName)
    }
  }
}
