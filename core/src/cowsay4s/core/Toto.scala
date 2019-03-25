package cowsay4s.core

object Toto {

  val muteTransformer: CowSay.CommandTransformer =
    (command: CowCommand) => command.copy(message = "")
  val thinkingTransformer: CowSay.CommandTransformer =
    _.copy(action = CowAction.CowThink)

  val thinkingAndMuteCowSay =
    CowSay.withTransformers(thinkingTransformer, muteTransformer)
}
