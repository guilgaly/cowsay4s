package cowsay4s.tests

import cowsay4s.core.Cow

object TestCow extends Cow {
  override def cowValue: String =
    "\n        $thoughts   ^__^\n         $thoughts  ($eyes)\\_______\n            (__)\\       )\\/\\\n             $tongue ||----w |\n                ||     ||\n"
}
