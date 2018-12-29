package cowsay4s.core.cows

/**
 *  A cow with a bong, from lars@csua.berkeley.edu
 */
private[core] object Bong extends DefaultCowContent {

  override def cowName: String = "bong"

  override def cowValue: String =
    """
      |         $thoughts
      |          $thoughts
      |            ^__^
      |    _______/($eyes)
      |/\\/(       /(__)
      |   | W----|| |~|
      |   ||     || |~|  ~~
      |             |~|  ~
      |             |_| o
      |             |#|/
      |            _+#+_
      |""".stripMargin
}
