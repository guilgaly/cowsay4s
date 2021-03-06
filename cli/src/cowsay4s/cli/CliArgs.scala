package cowsay4s.cli

import cowsay4s.core.{CowAction, CowEyes, CowMode, CowTongue}
import cowsay4s.core.defaults.{DefaultCow, DefaultCowMode}
import scopt.{OptionParser, Read}

case class CliArgs(
    mode: CowMode,
    eyes: Option[CowEyes],
    tongue: Option[CowTongue],
    cow: DefaultCow,
    list: Boolean,
    nowrap: Boolean,
    wrapcolumn: Int,
    action: CowAction,
    message: Option[String],
) {

  /** No-args constructor used only for the scopt parser. */
  private def this() = this(
    mode = DefaultCowMode.Default,
    eyes = None,
    tongue = None,
    cow = DefaultCow.Default,
    list = false,
    nowrap = false,
    wrapcolumn = 40,
    action = CowAction.CowSay,
    message = None,
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

      DefaultCowMode.values.foreach { mode =>
        modeSwitch(mode.modeName.charAt(0), mode.modeName, mode)
      }

      opt[CowEyes]('e', "eyes")
        .text(
          "appearance of the cow's eyes (only the first two characters are used)",
        )
        .action((eyes, config) => config.copy(eyes = Some(eyes)))

      opt[CowTongue]('T', "tongue")
        .text(
          "appearance of the cow's tongue (only the first two characters are used)",
        )
        .action((tongue, config) => config.copy(tongue = Some(tongue)))

      opt[DefaultCow]('f', "cowfile")
        .text("name of the cowfile to use")
        .action((cow, config) => config.copy(cow = cow))

      opt[Unit]('l', "list")
        .text("list all cowfiles")
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
              s"$w is not a valid argument for --wrapcolumn; should be strictly positive",
            )
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
              s"$invalid is not a valid action; should be 'say' or 'think'; ",
            )
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

  implicit val cowRead: Read[DefaultCow] = Read.reads { cowStr =>
    DefaultCow
      .withCowNameInsensitive(cowStr)
      .getOrElse(
        throw new IllegalArgumentException(
          s"'$cowStr' is not a valid cowfile name.",
        ),
      )
  }

  implicit val cowEyesRead: Read[CowEyes] = Read.reads(CowEyes(_))

  implicit val cowTongueRead: Read[CowTongue] = Read.reads(CowTongue(_))
}
