package cowsay4s.core.impl

import cowsay.tests.UnitSpec
import cowsay4s.core._

class TalkingCowSpec extends UnitSpec {

  "TalkingCow" when {
    "given a simple custom command" should {
      val command = CowCommand(
        action = CowAction.CowSay,
        provider = CowName("default"),
        face = CowMode.Stoned.face,
        wrap = StrictPositiveInt(40),
        message = "Cows love Scala!"
      )
      "construct a single line cowsay output" in {
        val cowsayCommand = command
        val expected = Right(
          """ __________________ 
            |< Cows love Scala! >
            | ------------------ 
            |        \   ^__^
            |         \  (**)\\_______
            |            (__)\\       )\\/\\
            |             U  ||----w |
            |                ||     ||
            |""".stripMargin
        )
        TalkingCow.printToString(cowsayCommand) shouldBe expected
      }
      "construct a multiline cowsay output" in {
        val cowsayCommand = command.copy(message =
          "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam nec cursus sapien, quis sagittis quam. In ultrices fringilla mauris, eget euismod mi faucibus vel. Nulla vel vulputate neque, nec feugiat massa.")
        val expected = Right(
          """ __________________________________________ 
            |/ Lorem ipsum dolor sit amet, consectetur  \
            || adipiscing elit. Aliquam nec cursus      |
            || sapien, quis sagittis quam. In ultrices  |
            || fringilla mauris, eget euismod mi        |
            || faucibus vel. Nulla vel vulputate neque, |
            |\ nec feugiat massa.                       /
            | ------------------------------------------ 
            |        \   ^__^
            |         \  (**)\\_______
            |            (__)\\       )\\/\\
            |             U  ||----w |
            |                ||     ||
            |""".stripMargin
        )
        TalkingCow.printToString(cowsayCommand) shouldBe expected
      }
      "construct a single line cowthink output" in {
        val cowthinkCommand = command.copy(action = CowAction.CowThink)
        val expected = Right(
          """ __________________ 
            |( Cows love Scala! )
            | ------------------ 
            |        o   ^__^
            |         o  (**)\\_______
            |            (__)\\       )\\/\\
            |             U  ||----w |
            |                ||     ||
            |""".stripMargin
        )
        TalkingCow.printToString(cowthinkCommand) shouldBe expected
      }
      "construct a multiline cowthink output" in {
        val cowsayCommand = command
          .copy(action = CowAction.CowThink)
          .copy(message =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam nec cursus sapien, quis sagittis quam. In ultrices fringilla mauris, eget euismod mi faucibus vel. Nulla vel vulputate neque, nec feugiat massa.")
        val expected = Right(
          """ __________________________________________ 
            |( Lorem ipsum dolor sit amet, consectetur  )
            |( adipiscing elit. Aliquam nec cursus      )
            |( sapien, quis sagittis quam. In ultrices  )
            |( fringilla mauris, eget euismod mi        )
            |( faucibus vel. Nulla vel vulputate neque, )
            |( nec feugiat massa.                       )
            | ------------------------------------------ 
            |        o   ^__^
            |         o  (**)\\_______
            |            (__)\\       )\\/\\
            |             U  ||----w |
            |                ||     ||
            |""".stripMargin
        )
        TalkingCow.printToString(cowsayCommand) shouldBe expected
      }
    }
  }
}
