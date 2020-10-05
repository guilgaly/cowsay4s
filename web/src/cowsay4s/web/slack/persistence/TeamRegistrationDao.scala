package cowsay4s.web.slack.persistence

import java.sql.{ResultSet, Timestamp}
import java.time.Instant
import java.util.{Calendar, TimeZone}

import scala.concurrent.Future

import cowsay4s.web.common.db.Database

final class TeamRegistrationDao(database: Database) {

  private val tzUTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

  private val table = "slack_team_registrations"

  private val teamIdCol = "team_id"
  private val teamNameCol = "team_name"
  private val createdOnCol = "created_on"
  private val updatedOnCol = "updated_on"
  private val accessTokenCol = "access_token"
  private val scopeCol = "scope"

  def insertOrUpdate(teamRegistration: TeamRegistrationLike): Future[Unit] =
    database.withTransactionAsync { connection =>
      val sql =
        s"""INSERT INTO $table (
           |  $teamIdCol,
           |  $teamNameCol,
           |  $createdOnCol,
           |  $updatedOnCol,
           |  $accessTokenCol,
           |  $scopeCol
           |)
           |VALUES (?, ?, ?, ?, ?, ?)
           |ON CONFLICT ($teamIdCol) DO UPDATE SET
           |  $teamNameCol = EXCLUDED.$teamNameCol,
           |  $updatedOnCol = EXCLUDED.$updatedOnCol,
           |  $accessTokenCol = EXCLUDED.$accessTokenCol,
           |  $scopeCol = EXCLUDED.$scopeCol""".stripMargin
      val stmt = connection.prepareStatement(sql)

      stmt.setString(1, teamRegistration.teamId)
      stmt.setString(2, teamRegistration.teamName)
      val now = new Timestamp(Instant.now().toEpochMilli)
      stmt.setTimestamp(3, now, tzUTC)
      stmt.setTimestamp(4, now, tzUTC)
      stmt.setString(5, teamRegistration.accessToken)
      stmt.setString(6, teamRegistration.scope)

      stmt.executeUpdate()
      stmt.close()
    }

  def get(teamId: String): Future[Option[TeamRegistration]] =
    database.withConnectionAsync { connection =>
      val sql = selectSql + s"\nWHERE $teamIdCol = ?"
      val stmt = connection.prepareStatement(sql)
      stmt.setString(1, teamId)

      val rs = stmt.executeQuery()
      val res = if (rs.next()) Some(mapRow(rs)) else None
      stmt.close()

      res
    }

  def list(): Future[Seq[TeamRegistration]] =
    database.withConnectionAsync { connection =>
      val stmt = connection.prepareStatement(selectSql)

      val rs = stmt.executeQuery()
      var res = List.empty[TeamRegistration]
      while (rs.next()) {
        res = mapRow(rs) +: res
      }
      stmt.close()

      res.reverse
    }

  private val selectSql =
    s"""SELECT
       |  $teamIdCol,
       |  $teamNameCol,
       |  $createdOnCol,
       |  $updatedOnCol,
       |  $accessTokenCol,
       |  $scopeCol
       |FROM $table""".stripMargin

  private def mapRow(rs: ResultSet): TeamRegistration = {
    val teamId = rs.getString(teamIdCol)
    val teamName = rs.getString(teamNameCol)
    val createdOn = rs.getTimestamp(createdOnCol, tzUTC).toInstant
    val updatedOn = rs.getTimestamp(updatedOnCol, tzUTC).toInstant
    val accessToken = rs.getString(accessTokenCol)
    val scope = rs.getString(scopeCol)
    TeamRegistration(teamId, teamName, createdOn, updatedOn, accessToken, scope)
  }
}
