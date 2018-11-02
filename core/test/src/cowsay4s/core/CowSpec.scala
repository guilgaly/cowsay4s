package cowsay4s.core

import cowsay.tests.UnitSpec
import cowsay4s.core.CowError.CowNotFound

class CowSpec extends UnitSpec {

  "loadFromClasspath" when {
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
        Cow.loadFromClasspath("default") shouldBe Right(Cow(expected))
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
        Cow.loadFromClasspath("satanic") shouldBe Right(Cow(expected))
      }
    }
    "given an invalid cow name" should {
      "return a 'CowNotFound' error" in {
        Cow.loadFromClasspath("toto") shouldBe Left(CowNotFound)
      }
    }
  }
}
