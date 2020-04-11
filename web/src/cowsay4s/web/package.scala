package cowsay4s

import akka.http.scaladsl.server.Route

package object web {
  type RouteProvider = () => Route
}
