package cowsay4s.core.impl

import cowsay4s.core.StrictPositiveInt
import cowsay4s.core.impl.Baloon.{DelimiterCouple, Delimiters}
import cowsay4s.tests.UnitSpec

class BaloonSpec extends UnitSpec {

  private val delimiters = new Delimiters {
    override def first: DelimiterCouple = DelimiterCouple('1', '2')
    override def middle: DelimiterCouple = DelimiterCouple('3', '4')
    override def last: DelimiterCouple = DelimiterCouple('5', '6')
    override def only: DelimiterCouple = DelimiterCouple('7', '8')
  }

  "format" when {
    "given an empty string" should {
      "format an empty baloon" in {
        val expected =
          """ __ 
            |7  8
            | -- """.stripMargin
        Baloon.format("", StrictPositiveInt(1), delimiters) shouldBe expected
      }
    }
    "given a short string" should {
      "format asingle line baloon" in {
        val str = "The cow says hello!"
        val expected =
          """ _____________________ 
            |7 The cow says hello! 8
            | --------------------- """.stripMargin
        Baloon.format(str, StrictPositiveInt(20), delimiters) shouldBe expected
      }
    }
    "given a long string" should {
      "wrap it properly" in {
        val str =
          "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum vel orci tempor, elementum odio in, bibendum odio. Pellentesque volutpat mattis nibh, eget dignissim est facilisis nec. Vivamus mollis iaculis lorem, vitae vehicula lorem tristique vel. Duis facilisis dignissim mauris, vel malesuada neque condimentum ac. Aliquam mollis luctus nibh euismod molestie. Fusce non consectetur dui, at suscipit urna. Ut ullamcorper est eu risus luctus, et accumsan enim pellentesque."
        val expected =
          """ __________________________________________ 
            |1 Lorem ipsum dolor sit amet, consectetur  2
            |3 adipiscing elit. Vestibulum vel orci     4
            |3 tempor, elementum odio in, bibendum      4
            |3 odio. Pellentesque volutpat mattis nibh, 4
            |3 eget dignissim est facilisis nec.        4
            |3 Vivamus mollis iaculis lorem, vitae      4
            |3 vehicula lorem tristique vel. Duis       4
            |3 facilisis dignissim mauris, vel          4
            |3 malesuada neque condimentum ac. Aliquam  4
            |3 mollis luctus nibh euismod molestie.     4
            |3 Fusce non consectetur dui, at suscipit   4
            |3 urna. Ut ullamcorper est eu risus        4
            |5 luctus, et accumsan enim pellentesque.   6
            | ------------------------------------------ """.stripMargin
        Baloon.format(str, StrictPositiveInt(40), delimiters) shouldBe expected
      }
    }
    "given a multi-line string" should {
      "pad lines properly" in {
        val str =
          """Lorem ipsum dolor sit amet,
            |adipiscing elit.
            |Vestibulum vel orci tempor,
            |elementum odio in,
            |bibendum odio""".stripMargin
        val expected =
          """ _____________________________ 
            |1 Lorem ipsum dolor sit amet, 2
            |3 adipiscing elit.            4
            |3 Vestibulum vel orci tempor, 4
            |3 elementum odio in,          4
            |5 bibendum odio               6
            | ----------------------------- """.stripMargin
        Baloon.format(str, StrictPositiveInt(40), delimiters) shouldBe expected
      }
    }
  }
}
