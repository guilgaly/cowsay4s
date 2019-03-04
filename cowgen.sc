import java.nio.charset.StandardCharsets

import $ivy.`org.apache.commons:commons-text:1.6`
import org.apache.commons.text.{CaseUtils, StringEscapeUtils}

def generateDefaultCows(dir: os.Path, cowfiles: Seq[os.Path]): Unit = {

  def loadCowFromFile(cowName: String, cowfile: os.Path) = {
    val rawCowString =
      new String(ammonite.ops.read.bytes(cowfile), StandardCharsets.UTF_8)
    val cowString = rawCowString.replace("\r\n", "\n").replace('\r', '\n')

    val pattern = """(?s).*\$the_cow\s*=\s*<<"?EOC"?;?(.*)EOC.*""".r

    val filteredCowString = cowString match {
      case pattern(cowValue) => cowValue
      case _                 => cowString
    }

    filteredCowString
      .replace("${eyes}", "$eyes")
      .replace("${tongue}", "$tongue")
  }

  def generateCowContents() = cowfiles.map { cowfile =>
    val cowName = cowfile.last.dropRight(4)
    val scalaName = CaseUtils.toCamelCase(cowName, true, '.', '-', '_')
    val rawCowValue = loadCowFromFile(cowName, cowfile)
    val unescapedCowValue = unescapePerlString(rawCowValue)
    val cowValue = StringEscapeUtils.escapeJava(unescapedCowValue)

    val scalaObjectSource =
      s"""package cowsay4s.defaults.cows
         |
         |private[defaults] object $scalaName extends DefaultCowContent {
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
      s"""package cowsay4s.defaults
         |
         |import cowsay4s.core.Cow
         |import cowsay4s.core.EnumWithDefault
         |import cowsay4s.defaults.cows.DefaultCowContent
         |import enumeratum.EnumEntry
         |import scala.collection.immutable
         |
         |sealed abstract class DefaultCow(content: DefaultCowContent)
         |    extends EnumEntry
         |    with Cow {
         |  def cowName: String = content.cowName
         |  override def cowValue: String = content.cowValue
         |}
         |
         |object DefaultCow extends EnumWithDefault[DefaultCow] {
         |
         |$cowObjects
         |
         |  override def values: immutable.IndexedSeq[DefaultCow] = findValues
         |
         |  override def defaultValue: DefaultCow = Default
         |
         |  lazy val cowNames: immutable.IndexedSeq[String] = values.map(_.cowName)
         |
         |  lazy val cowNamesToValuesMap: Map[String, DefaultCow] =
         |    values.map(v => v.cowName -> v).toMap
         |
         |  lazy val lowerCaseCowNamesToValuesMap: Map[String, DefaultCow] =
         |    cowNamesToValuesMap.map{ case (k, v) => k.toLowerCase -> v }
         |
         |  def withCowName(cowName: String): Option[DefaultCow] =
         |    cowNamesToValuesMap.get(cowName)
         |
         |  def withCowNameInsensitive(cowName: String): Option[DefaultCow] =
         |    lowerCaseCowNamesToValuesMap.get(cowName.toLowerCase)
         |}""".stripMargin

    val scalaFile = dir / "DefaultCow.scala"
    ammonite.ops.write(scalaFile, scalaEnumSource, createFolders = true)
  }

  val scalaNames = generateCowContents()
  generateDefaultCowEnum(scalaNames)
}
