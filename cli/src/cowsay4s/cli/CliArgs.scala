package cowsay4s.cli
import cowsay4s.core.{CowAction, CowMode}
import scopt.OptionParser

case class CliArgs(
    mode: CowMode,
    eyes: Option[String],
    tongue: Option[String],
    cowfile: String,
    list: Boolean,
    nowrap: Boolean,
    wrapcolumn: Int,
    action: CowAction,
    message: Option[String]
) {

  /** No-args constructor used only for the scopt parser. */
  private def this() = this(
    mode = CowMode.Default,
    eyes = None,
    tongue = None,
    cowfile = "default",
    list = false,
    nowrap = false,
    wrapcolumn = 40,
    action = CowAction.CowSay,
    message = None
  )
}

object CliArgs {

  /**
   * Parses the command line arguments.
   *
   * @param args The command line arguments
   * @return The configuration parsed from the given `args`
   */
  def parse(args: Seq[String]): Option[CliArgs] =
    parser.parse(args, new CliArgs())

  private def parser: OptionParser[CliArgs] =
    new OptionParser[CliArgs]("cowsay4s-cli") {
      head("cowsay4s-cli")

      help("help").abbr("h").text("prints this usage text")

      modeSwitch('b', "borg", CowMode.Borg)
      modeSwitch('d', "dead", CowMode.Dead)
      modeSwitch('g', "greedy", CowMode.Greedy)
      modeSwitch('p', "paranoia", CowMode.Paranoia)
      modeSwitch('s', "stoned", CowMode.Stoned)
      modeSwitch('t', "tired", CowMode.Tired)
      modeSwitch('w', "wired", CowMode.Wired)
      modeSwitch('y', "youthful", CowMode.Youthful)

      opt[String]('e', "eyes")
        .text(
          "appearance of the cow's eyes (only the first two characters are used)")
        .action((eyes, config) => config.copy(eyes = Some(eyes)))

      opt[String]('T', "tongue")
        .text(
          "appearance of the cow's tongue (only the first two characters are used)")
        .action((tongue, config) => config.copy(tongue = Some(tongue)))

      opt[String]('f', "cowfile")
        .text("name of the cowfile to use")
        .action((cowfile, config) => config.copy(cowfile = cowfile))

      opt[Unit]('l', "list")
        .text("list all cowfiles [NOT IMPLEMENTED YET]")
        .action((_, config) => config.copy(list = true))

      opt[Unit]('n', "nowrap")
        .text("do not automatically wrap the message [NOT IMPLEMENTED YET]")
        .action((_, config) => config.copy(nowrap = true))

      opt[Int]('W', "wrapcolumn")
        .text("max line length to wrap the message (40 by default)")
        .validate { w =>
          if (w > 0)
            success
          else
            failure(
              s"$w is not a valid argument for --wrapcolumn; should be strictly positive")
        }
        .action((wrapcolumn, config) => config.copy(wrapcolumn = wrapcolumn))

      opt[String]('a', "action")
        .valueName("<say|think>")
        .text("whether the cow should say or think the message")
        .validate {
          case "say" | "think" =>
            success
          case invalid =>
            failure(
              s"$invalid is not a valid action; should be 'say' or 'think'; ")
        }
        .action {
          case ("say", config)   => config.copy(action = CowAction.CowSay)
          case ("think", config) => config.copy(action = CowAction.CowThink)
          case (_, config)       => config
        }

      arg[String]("<message>")
        .optional()
        .text("the message the cow will say or think")
        .action((message, config) => config.copy(message = Some(message)))

      private def modeSwitch(x: Char, name: String, mode: CowMode) =
        opt[Unit](x, name)
          .text(s"$name mode")
          .action((_, config: CliArgs) => config.copy(mode = mode))
    }
}
