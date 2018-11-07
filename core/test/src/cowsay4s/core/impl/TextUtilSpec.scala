package cowsay4s.core.impl

import cowsay.tests.UnitSpec

class TextUtilSpec extends UnitSpec {

  "wrap" when {
    "given an empty string" should {
      "return a single empty string" in {
        TextUtil.softWrap("", 1) shouldBe Seq("")
      }
    }
    "given a short string" should {
      "return a single string" in {
        val str = "The cow says hello!"
        val expected = Seq(str)
        TextUtil.softWrap(str, 20) shouldBe expected
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
          "luctus, et accumsan enim pellentesque."
        )
        TextUtil.softWrap(str, 40) shouldBe expected
      }
    }
    "given a string with multipoint unicode characters" should {
      "wrap it properly" in {
        val str = "😀😀😀😀 🐮🐮🐮🐮 🤘🤘🤘🤘 👽👽👽👽👽 🦄🦄🦄"
        val expected = Seq(
          "😀😀😀😀 🐮🐮🐮🐮",
          "🤘🤘🤘🤘 👽👽👽👽👽",
          "🦄🦄🦄"
        )
        TextUtil.softWrap(str, 10) shouldBe expected
      }
    }
  }

  "displayLength" when {
    "given an empty string" should {
      "return 0" in {
        TextUtil.displayLength("") shouldBe 0
      }
    }
    "given a simple string" should {
      "return the correct length" in {
        TextUtil.displayLength("Cows love Scala!") shouldBe 16
      }
    }
    "given a string with multipoint unicode characters" should {
      "return the correct length" in {
        TextUtil.displayLength("Cows ♥︎ Scala! 🐮") shouldBe 15
      }
    }
  }

  "padToDisplayLength" when {
    "given an empty string" should {
      "pad it with spaces" in {
        TextUtil.padToDisplayLength("", 10) shouldBe "          "
      }
    }
    "given a too long string" should {
      "return it as-is" in {
        TextUtil.padToDisplayLength("This 🗨 is way too long! 😱", 10) shouldBe
          "This 🗨 is way too long! 😱"
      }
    }
    "given an exact string" should {
      "return it as-is" in {
        TextUtil.padToDisplayLength("0123456789", 10) shouldBe "0123456789"
      }
    }
    "given a short string with multipoint unicode characters" should {
      "pad it to the correct display length" in {
        TextUtil.padToDisplayLength("Cows ♥︎ Scala! 🐮", 20) shouldBe
          "Cows ♥︎ Scala! 🐮     "
      }
    }
  }

  "normalizeToDisplayLength" when {
    "given an empty string" should {
      "pad it with spaces" in {
        TextUtil.normalizeToDisplayLength("", 10) shouldBe "          "
      }
    }
    "given a too long string with multipoint unicode characters" should {
      "cut it to length" in {
        TextUtil.normalizeToDisplayLength("This 🗨 is way too long! 😱", 10) shouldBe
          "This 🗨 is "
      }
    }
    "given an exact string" should {
      "return it as-is" in {
        TextUtil.normalizeToDisplayLength("0123456789", 10) shouldBe "0123456789"
      }
    }
    "given a short string with multipoint unicode characters" should {
      "pad it to the correct display length" in {
        TextUtil.normalizeToDisplayLength("Cows ♥︎ Scala! 🐮", 20) shouldBe
          "Cows ♥︎ Scala! 🐮     "
      }
    }
  }
}
