package cowsay4s.core.cows

private[core] object Default extends DefaultCowContent {

  override def cowName: String = "default"

  override def cowValue: String =
    """
      |        $thoughts   ^__^
      |         $thoughts  ($eyes)\\_______
      |            (__)\\       )\\/\\
      |             $tongue ||----w |
      |                ||     ||
      |""".stripMargin
}
