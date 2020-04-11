package cowsay4s.web

import com.typesafe.config.Config

final class ServerSettings(val config: Config) {

  val secret: String = config.getString("secret")

  val baseUrl: String = config.getString("baseUrl")

  object http {
    val interface: String = config.getString("http.interface")
    val port: Int = config.getInt("http.port")
  }

  object database {
    val jdbcUrl: String = config.getString("database.jdbcUrl")
  }

  object slack {
    val appId: String = config.getString("slack.appId")
    val clientId: String = config.getString("slack.clientId")
    val clientSecret: String = config.getString("slack.clientSecret")
    val signingSecret: String = config.getString("slack.signingSecret")
    val appInstallUrl: String = config.getString("slack.appInstallUrl")
  }
}
