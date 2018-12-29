package cowsay4s.core.cows

/**
 * Beavis, with Zen philosophy removed.
 */
private[core] object BeavisZen extends DefaultCowContent {

  override def cowName: String = "beavis.zen"

  override def cowValue: String =
    """
      |   $thoughts         __------~~-,
      |    $thoughts      ,'            ,
      |          /               \\
      |         /                :
      |        |                  '
      |        |                  |
      |        |                  |
      |         |   _--           |
      |         _| =-.     .-.   ||
      |         o|/o/       _.   |
      |         /  ~          \\ |
      |       (____\@)  ___~    |
      |          |_===~~~.`    |
      |       _______.--~     |
      |       \\________       |
      |                \\      |
      |              __/-___-- -__
      |             /            _ \\
      |""".stripMargin
}
