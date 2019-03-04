package cowsay4s.core

trait CowMode {
  def eyes: CowEyes
  def tongue: CowTongue
}
