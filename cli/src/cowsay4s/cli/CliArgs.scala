package cowsay4s.cli
import cowsay4s.core.{CowAction, CowMode}
import scopt.OptionParser

case class CliArgs(
    mode: Option[CowMode],
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
    mode = None,
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

//  Usage: cowsay [-bdgpstwy] [-h] [-e eyes] [-f cowfile]
//    [-l] [-n] [-T tongue] [-W wrapcolumn] [message]

  private def parser = new OptionParser[CliArgs]("cowsay4s-cli") {
    head("cowsay4s-cli")

    help("help").text("prints this usage text")

    arg[String]("<say | think>")
      .text("wether the cow should say or think")
      .validate {
        case "say" | "think" => success
        case _               => failure("action must be 'say' or 'think'")
      }
      .action {
        case ("say", config)   => config.copy(action = CowAction.CowSay)
        case ("think", config) => config.copy(action = CowAction.CowThink)
        case (_, config)       => config
      }

    arg[String]("<message>")
      .text("the message the cow will say or think")
      .action((string: String, config: CliArgs) =>
        config.copy(message = Some(string)))
  }
}
