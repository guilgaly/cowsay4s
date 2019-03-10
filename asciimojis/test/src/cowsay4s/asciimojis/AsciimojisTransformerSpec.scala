package cowsay4s.asciimojis

import cowsay4s.testutils.UnitSpec

class AsciimojisTransformerSpec extends UnitSpec {

  "AsciimojisTransformer" when {
    "given a string with no emojis" should {
      "return it as-is" in {
        val str =
          "Lorem (ipsum dolor) sit amet, (consectetur) ((adipiscing) elit))). (Vestibulum vel orci tempor, elementum odio in, bibendum odio."
        AsciimojisTransformer.replace(str) shouldBe str
      }
    }
    "given a string with some emojis" should {
      "replace them" in {
        val str =
          "(butterfly)Lorem (ipsum dolor) sit (acid)((cat)) amet, (consectetur) ((adipiscing) elit))). (Vestibulum vel orci tempor, elementum odio in, bibendum odio."
        val expected =
          "ƸӜƷLorem (ipsum dolor) sit ⊂(◉‿◉)つ((= ФェФ=)) amet, (consectetur) ((adipiscing) elit))). (Vestibulum vel orci tempor, elementum odio in, bibendum odio."
        AsciimojisTransformer.replace(str) shouldBe expected
      }
    }
  }
}
