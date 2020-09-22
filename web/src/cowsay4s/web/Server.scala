package cowsay4s.web

import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import org.log4s._

import scala.concurrent.Await
import scala.concurrent.duration._

object Server extends RootModule {
  private[this] val log = getLogger

  def main(args: Array[String]): Unit = {
    val interface = settings.http.interface
    val port = settings.http.port

    val binding: ServerBinding =
      Await.result(Http().newServerAt(interface, port).bind(allRoutes), 30.seconds)
    log.info(s"HTTP server bound to ${binding.localAddress}")

    Await.result(system.whenTerminated, Duration.Inf)
    Await.result(binding.terminate(15.seconds), 20.seconds)
    ()
  }
}
