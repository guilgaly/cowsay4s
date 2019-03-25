package cowsay4s.asciimojis

import cowsay4s.testutils.UnitSpec
import org.scalatest.prop.TableDrivenPropertyChecks

class AsciimojisTransformerSpec
    extends UnitSpec
    with TableDrivenPropertyChecks {

  "AsciimojisTransformer" when {
    "given a string with no asciimojis" should {
      "return it as-is" in {
        val str =
          "Lorem (ipsum dolor) sit amet, (consectetur) ((adipiscing) elit))). (Vestibulum vel orci tempor, elementum odio in, bibendum odio."
        AsciimojisTransformer.replace(str) shouldBe str
      }
    }
    "given a string with some asciimojis" should {
      "replace them" in {
        val str =
          "(butterfly)Lorem (ipsum dolor) sit (acid)((cat)) amet, (consectetur) ((adipiscing) elit))). (Vestibulum vel orci tempor, elementum odio in, bibendum odio."
        val expected =
          "ƸӜƷLorem (ipsum dolor) sit ⊂(◉‿◉)つ((= ФェФ=)) amet, (consectetur) ((adipiscing) elit))). (Vestibulum vel orci tempor, elementum odio in, bibendum odio."
        AsciimojisTransformer.replace(str) shouldBe expected
      }
    }
    "given any asciimoji" should {
      "replace it" in {
        val asciimojis = Table(
          ("word", "asciimoji"),
          (for {
            asciimoji <- AsciimojisTransformer.asciimojis
              .filterNot(_.name == "rolldice")
            word <- asciimoji.words
          } yield (word, asciimoji)): _*
        )
        forAll(asciimojis) {
          case (word, s: Asciimoji.Simple) =>
            AsciimojisTransformer.replace(s"==($word)==") shouldBe s"==${s.ascii}=="
          case (word, p: Asciimoji.Parameterized) =>
            AsciimojisTransformer.replace(s"==($word)==") shouldBe s"==${p.render(None)}=="
            AsciimojisTransformer.replace(s"==($word,${p.exampleParam})==") shouldBe
              s"==${p.render(Some(p.exampleParam))}=="
        }
      }
    }
  }
}
