package cowsay4s.web.common.db

import java.sql.Connection

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import cowsay4s.web.ServerSettings
import org.log4s._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.{ControlThrowable, NonFatal}

final class Database(settings: ServerSettings, ec: ExecutionContext) {
  private[this] val log = getLogger

  private val dataSource = {
    val config = new HikariConfig()
    config.setJdbcUrl(settings.database.jdbcUrl)
    config.setAutoCommit(false)

    new HikariDataSource(config)
  }

  initIfNecessary()

  def getConnection(): Connection =
    dataSource.getConnection()

  def withConnection[A](block: Connection => A): A = {
    val connection = getConnection()
    try {
      block(connection)
    } finally {
      try {
        connection.close()
      } catch {
        case NonFatal(e) =>
          log.error(e)(s"Error while trying to close JDBC connection")
      }
    }
  }

  def withConnectionAsync[A](block: Connection => A): Future[A] =
    Future(withConnection(block))(ec)

  def withTransaction[A](block: Connection => A): A = {
    withConnection { connection =>
      try {
        val r = block(connection)
        connection.commit()
        r
      } catch {
        case e: ControlThrowable =>
          connection.commit()
          throw e
        case e: Throwable =>
          connection.rollback()
          throw e
      }
    }
  }

  def withTransactionAsync[A](block: Connection => A): Future[A] =
    Future(withTransaction(block))(ec)

  def close(): Unit =
    dataSource.close()

  private def initIfNecessary(): Unit = withTransaction { connection =>
    val dbMetadata = connection.getMetaData()

    // Table slack_tokens
    val res = dbMetadata
      .getTables(null, null, "slack_team_registrations", Array("TABLE"))
    if (!res.next()) {
      log.info("Creating database table 'slack_team_registrations'...")
      val sql =
        """CREATE TABLE slack_team_registrations (
          |  team_id TEXT PRIMARY KEY,
          |  team_name TEXT NOT NULL,
          |  created_on TIMESTAMP WITH TIME ZONE NOT NULL,
          |  updated_on TIMESTAMP WITH TIME ZONE NOT NULL,
          |  access_token TEXT NOT NULL,
          |  scope TEXT NOT NULL
          |)""".stripMargin
      val stmt = connection.prepareStatement(sql)
      stmt.executeUpdate()
      stmt.close()
      log.info("Created database table 'slack_team_registrations'.")
    }
  }
}
