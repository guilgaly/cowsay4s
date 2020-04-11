package cowsay4s.web.slack.model

import cowsay4s.core.defaults.{DefaultCow, DefaultCowMode}
import cowsay4s.testutils.UnitSpec

class TalkCommandTextParserSpec extends UnitSpec {
  import TalkCommandText.ParsingError._

  "The parser" should {

    "parse a command" when {
      "given a command string without options" in {
        val text = "   Hello World!\n\tWhat a lovely test. ♥︎  "
        val expected = TalkCommandText.withDefaults(
          None,
          None,
          "Hello World!\n\tWhat a lovely test. ♥︎"
        )
        TalkCommandText.Parser(text) shouldBe Right(expected)
      }
      "given a string with options" in {
        val text =
          "cow=elephant-in-snake\nmode=borg  \t Hello World!\n\tWhat a lovely test. ♥︎  "
        val expected = TalkCommandText(
          DefaultCow.ElephantInSnake,
          DefaultCowMode.Borg,
          "Hello World!\n\tWhat a lovely test. ♥︎"
        )
        TalkCommandText.Parser(text) shouldBe Right(expected)
      }
      "given a string with options in a different order" in {
        val text =
          "mode=stoned cow=pterodactyl Hello World!"
        val expected = TalkCommandText(
          DefaultCow.Pterodactyl,
          DefaultCowMode.Stoned,
          "Hello World!"
        )
        TalkCommandText.Parser(text) shouldBe Right(expected)
      }
    }

    "reject a command" when {
      "given a string with an illegal option" in {
        val text = "cow=toto mode=borg Hello."
        val expected = Left(List(InvalidCow("toto")))
        TalkCommandText.Parser(text) shouldBe expected
      }
    }
  }
}
