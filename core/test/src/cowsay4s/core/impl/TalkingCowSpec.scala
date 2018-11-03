package cowsay4s.core.impl
import cowsay.tests.UnitSpec
import cowsay4s.core._

class TalkingCowSpec extends UnitSpec {

  "" in {
    val command = CowCommand(
      action = CowAction.CowSay,
      provider = CowName("default"),
      face = CowMode.Stoned.face,
      wrap = StrictPositiveInt(40),
      message = "Cows love Scala!"
    )
    TalkingCow.printToString(command) match {
      case Left(err)    => println(err)
      case Right(value) => println(value)
    }
  }
}
