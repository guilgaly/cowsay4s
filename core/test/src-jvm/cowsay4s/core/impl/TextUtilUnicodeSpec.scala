package cowsay4s.core.impl

import cowsay4s.testutils.UnitSpec

/**
 * Tests proper unicode support (mainly: a multipoint unicode character should
 * count as a single character for line length calculations.)
 *
 * Currently only works on the JVM.
 */
class TextUtilUnicodeSpec extends UnitSpec {
  "wrap" when {
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
    "given a string with multipoint unicode characters" should {
      "return the correct length" in {
        TextUtil.displayLength("Cows ♥︎ Scala! 🐮") shouldBe 15
      }
    }
  }

  "padToDisplayLength" when {
    "given a too long string" should {
      "return it as-is" in {
        TextUtil.padToDisplayLength("This 🗨 is way too long! 😱", 10) shouldBe
          "This 🗨 is way too long! 😱"
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
    "given a too long string with multipoint unicode characters" should {
      "cut it to length" in {
        TextUtil.normalizeToDisplayLength("This 🗨 is way too long! 😱", 10) shouldBe
          "This 🗨 is "
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
