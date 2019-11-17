package cowsay4s.core.impl

import java.io.{PrintWriter, StringWriter}

private[core] object ThrowableUtil {
  def printStackTrace(t: Throwable): String = {
    val sw = new StringWriter
    t.printStackTrace(new PrintWriter(sw))
    sw.toString
  }
}
