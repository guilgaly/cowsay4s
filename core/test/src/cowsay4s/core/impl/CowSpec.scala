package cowsay4s.core.impl

import cowsay.tests.UnitSpec
import cowsay4s.core.CowError.CowNotFound
import cowsay4s.core.CowName

class CowSpec extends UnitSpec {

  "load" when {
    "given a valid cow name" should {
      "load the 'default' cow" in {
        val expected =
          """
            |        $thoughts   ^__^
            |         $thoughts  ($eyes)\\_______
            |            (__)\\       )\\/\\
            |             $tongue ||----w |
            |                ||     ||
            |""".stripMargin
        Cow.load(CowName("default")) shouldBe Right(Cow(expected))
      }
      "load the 'satanic' cow" in {
        val expected =
          """
            |     $thoughts
            |      $thoughts  (__)  
            |         (\\/)  
            |  /-------\\/    
            | / | 666 ||    
            |*  ||----||      
            |   ~~    ~~      
            |""".stripMargin
        Cow.load(CowName("satanic")) shouldBe Right(Cow(expected))
      }
    }
    "given an invalid cow name" should {
      "return a 'CowNotFound' error" in {
        Cow.load(CowName("toto")) shouldBe Left(CowNotFound)
      }
    }
  }
}
