package cowsay4s.web.slack.model

case class TalkCommand(
    slashCommand: SlashCommand,
    text: String,
    userId: String,
    teamId: String,
    responseUrl: String,
)
