package cowsay4s.web.slack.persistence

import java.time.Instant

sealed trait TeamRegistrationLike {
  def teamId: String
  def teamName: String
  def accessToken: String
  def scope: String
}

final case class NewTeamRegistration(
    teamId: String,
    teamName: String,
    accessToken: String,
    scope: String
) extends TeamRegistrationLike

final case class TeamRegistration(
    teamId: String,
    teamName: String,
    createdOn: Instant,
    updatedOn: Instant,
    accessToken: String,
    scope: String
) extends TeamRegistrationLike
