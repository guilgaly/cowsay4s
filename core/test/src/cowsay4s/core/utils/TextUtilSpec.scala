package cowsay4s.core.utils

import cowsay.tests.UnitSpec

class TextUtilSpec extends UnitSpec {

  "wrap" when {
    "given an empty string" should {
      "return a single empty string" in {
        TextUtil.wrap("", 0) shouldBe Seq("")
      }
    }
    "given a short string" should {
      "return a single string" in {
        val str = "The cow says hello!"
        val expected = Seq(str)
        TextUtil.wrap(str, 20) shouldBe expected
      }
    }
    "given a long string" should {
      "wrap it properly" in {
        val str =
          "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum vel orci tempor, elementum odio in, bibendum odio. Pellentesque volutpat mattis nibh, eget dignissim est facilisis nec. Vivamus mollis iaculis lorem, vitae vehicula lorem tristique vel. Duis facilisis dignissim mauris, vel malesuada neque condimentum ac. Aliquam mollis luctus nibh euismod molestie. Fusce non consectetur dui, at suscipit urna. Ut ullamcorper est eu risus luctus, et accumsan enim pellentesque."
        val expected = Seq(
          "Lorem ipsum dolor sit amet, consectetur",
          "adipiscing elit. Vestibulum vel orci",
          "tempor, elementum odio in, bibendum",
          "odio. Pellentesque volutpat mattis nibh,",
          "eget dignissim est facilisis nec.",
          "Vivamus mollis iaculis lorem, vitae",
          "vehicula lorem tristique vel. Duis",
          "facilisis dignissim mauris, vel",
          "malesuada neque condimentum ac. Aliquam",
          "mollis luctus nibh euismod molestie.",
          "Fusce non consectetur dui, at suscipit",
          "urna. Ut ullamcorper est eu risus",
          "luctus, et accumsan enim pellentesque.",
        )
        TextUtil.wrap(str, 40) shouldBe expected
      }
    }
  }
}
