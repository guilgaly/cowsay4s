package cowsay4s.core.impl

import cowsay4s.core.{
  CowAction,
  CowCommand,
  CowEyes,
  CowTongue,
  MessageWrapping,
  TestCow,
}
import cowsay4s.testutils.UnitSpec

class TalkingCowSpec extends UnitSpec {
  "TalkingCow" when {
    "given a simple custom command" should {
      val command = CowCommand(
        TestCow,
        "Cows love Scala!",
        CowEyes("**"),
        CowTongue("U "),
        CowAction.CowSay,
        MessageWrapping(40),
      )
      "construct a single line cowsay output" in {
        val cowsayCommand = command
        val expected =
          """ __________________ 
            |< Cows love Scala! >
            | ------------------ 
            |        \   ^__^
            |         \  (**)\_______
            |            (__)\       )\/\
            |             U  ||----w |
            |                ||     ||
            |""".stripMargin
        TalkingCow.printToString(cowsayCommand) shouldBe expected
      }
      "construct a multiline cowsay output" in {
        val cowsayCommand = command.copy(
          message =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam nec cursus sapien, quis sagittis quam. In ultrices fringilla mauris, eget euismod mi faucibus vel. Nulla vel vulputate neque, nec feugiat massa.",
        )
        val expected =
          """ __________________________________________ 
            |/ Lorem ipsum dolor sit amet, consectetur  \
            || adipiscing elit. Aliquam nec cursus      |
            || sapien, quis sagittis quam. In ultrices  |
            || fringilla mauris, eget euismod mi        |
            || faucibus vel. Nulla vel vulputate neque, |
            |\ nec feugiat massa.                       /
            | ------------------------------------------ 
            |        \   ^__^
            |         \  (**)\_______
            |            (__)\       )\/\
            |             U  ||----w |
            |                ||     ||
            |""".stripMargin
        TalkingCow.printToString(cowsayCommand) shouldBe expected
      }
      "construct a single line cowthink output" in {
        val cowthinkCommand = command.copy(action = CowAction.CowThink)
        val expected =
          """ __________________ 
            |( Cows love Scala! )
            | ------------------ 
            |        o   ^__^
            |         o  (**)\_______
            |            (__)\       )\/\
            |             U  ||----w |
            |                ||     ||
            |""".stripMargin
        TalkingCow.printToString(cowthinkCommand) shouldBe expected
      }
      "construct a multiline cowthink output" in {
        val cowsayCommand = command
          .copy(action = CowAction.CowThink)
          .copy(
            message =
              "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam nec cursus sapien, quis sagittis quam. In ultrices fringilla mauris, eget euismod mi faucibus vel. Nulla vel vulputate neque, nec feugiat massa.",
          )
        val expected =
          """ __________________________________________ 
            |( Lorem ipsum dolor sit amet, consectetur  )
            |( adipiscing elit. Aliquam nec cursus      )
            |( sapien, quis sagittis quam. In ultrices  )
            |( fringilla mauris, eget euismod mi        )
            |( faucibus vel. Nulla vel vulputate neque, )
            |( nec feugiat massa.                       )
            | ------------------------------------------ 
            |        o   ^__^
            |         o  (**)\_______
            |            (__)\       )\/\
            |             U  ||----w |
            |                ||     ||
            |""".stripMargin
        TalkingCow.printToString(cowsayCommand) shouldBe expected
      }
    }
  }
}
