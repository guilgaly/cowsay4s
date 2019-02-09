import java.nio.charset.StandardCharsets

import $ivy.`org.apache.commons:commons-text:1.6`
import org.apache.commons.text.{CaseUtils, StringEscapeUtils}

def generateDefaultCows(dir: os.Path, cowfiles: Seq[os.Path]): Unit = {

  def loadCowFromFile(cowName: String, cowfile: os.Path) = {
    val rawCowString =
      new String(ammonite.ops.read.bytes(cowfile), StandardCharsets.UTF_8)
    val cowString = rawCowString.replace("\r\n", "\n").replace('\r', '\n')

    val pattern = """(?s).*\$the_cow\s*=\s*<<"?EOC"?;(.*)EOC.*""".r

    cowString match {
      case pattern(cowValue) => cowValue
      case _                 => cowString
    }
  }

  def generateCowContents() = cowfiles.map { cowfile =>
    val cowName = cowfile.last.dropRight(4)
    val scalaName = CaseUtils.toCamelCase(cowName, true, '.', '-', '_')
    val rawCowValue = loadCowFromFile(cowName, cowfile)
    val unescapedCowValue = unescapePerlString(rawCowValue)
    val cowValue = StringEscapeUtils.escapeJava(unescapedCowValue)

    val scalaObjectSource =
      s"""package cowsay4s.core.cows
         |
         |private[core] object $scalaName extends DefaultCowContent {
         |
         |  override def cowName: String = "$cowName"
         |
         |  override def cowValue: String = "$cowValue"
         |}""".stripMargin

    val scalaFile = dir / 'cows / s"$scalaName.scala"
    ammonite.ops.write(scalaFile, scalaObjectSource, createFolders = true)

    scalaName
  }

  def unescapePerlString(str: String) = {
    val regex = """(\\)(.)""".r
    regex.replaceAllIn(str, "$2")
  }

  def generateDefaultCowEnum(scalaNames: Seq[String]): Unit = {
    val cowObjects =
      scalaNames
        .map { scalaName =>
          s"  case object $scalaName extends DefaultCow(cows.$scalaName)"
        }
        .mkString("\n")

    val scalaEnumSource =
      s"""package cowsay4s.core
         |
         |import cowsay4s.core.cows.DefaultCowContent
         |import enumeratum.{Enum, EnumEntry}
         |import scala.collection.immutable
         |
         |sealed abstract class DefaultCow(content: DefaultCowContent)
         |    extends EnumEntry
         |    with Cow {
         |  def cowName: String = content.cowName
         |  override def cowValue: String = content.cowValue
         |}
         |
         |object DefaultCow extends Enum[DefaultCow] {
         |
         |$cowObjects
         |
         |  override def values: immutable.IndexedSeq[DefaultCow] = findValues
         |
         |  def cowNames: immutable.IndexedSeq[String] = values.map(_.cowName)
         |
         |  def withCowName(cowName: String): Option[DefaultCow] =
         |    values.find(_.cowName == cowName)
         |}""".stripMargin

    val scalaFile = dir / "DefaultCow.scala"
    ammonite.ops.write(scalaFile, scalaEnumSource, createFolders = true)
  }

  val scalaNames = generateCowContents()
  generateDefaultCowEnum(scalaNames)
}
