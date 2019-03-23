package cowsay4s.asciimojis

sealed trait Asciimoji {
  def name: String
  def words: Seq[String]
}

object Asciimoji {

  class Simple(
      override val name: String,
      override val words: Seq[String],
      val ascii: String
  ) extends Asciimoji {
    override def toString: String = s"Asciimoji.Simple($name)"
  }

  object Simple {
    def apply(name: String, words: Seq[String], ascii: String): Simple =
      new Simple(name, words, ascii)
    def apply(name: String, word: String, ascii: String): Simple =
      new Simple(name, Seq(word), ascii)
  }

  class Parameterized(
      override val name: String,
      override val words: Seq[String],
      val exampleParam: String,
      val render: Option[String] => String
  ) extends Asciimoji {
    override def toString: String = s"Asciimoji.Parameterized($name)"
  }

  object Parameterized {
    def apply(
        name: String,
        words: Seq[String],
        exampleParam: String,
        renderFunction: Option[String] => String
    ): Parameterized =
      new Parameterized(name, words, exampleParam, renderFunction)
  }
}
