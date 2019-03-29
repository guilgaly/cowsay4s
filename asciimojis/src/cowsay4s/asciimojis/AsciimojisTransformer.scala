package cowsay4s.asciimojis

import cowsay4s.core.CowCommand
import cowsay4s.core.CowSay.CommandTransformer

import scala.util.matching.Regex
import scala.util.{Random, Try}

object AsciimojisTransformer extends CommandTransformer {

  override def apply(command: CowCommand): CowCommand =
    command.copy(message = replace(command.message))

  private val pattern = """\(([^\(\),]+)(?:,([^\(\)]*))?\)""".r("word", "param")

  private[asciimojis] def replace(text: String) = {
    val replacer = (mtch: Regex.Match) => {
      val word = mtch.group("word")
      dictionnary
        .get(word)
        .map { asciimoji =>
          val replacement = asciimoji match {
            case s: Asciimoji.Simple =>
              s.ascii
            case p: Asciimoji.Parameterized =>
              val param = Option(mtch.group("param"))
              p.render(param)
          }
          Regex.quoteReplacement(replacement)
        }
        .getOrElse(s"($word)")
    }
    pattern.replaceAllIn(text, replacer)
  }

  val asciimojis: Seq[Asciimoji] = Seq(
    Asciimoji.Simple("acid", Seq("acid"), "⊂(◉‿◉)つ"),
    Asciimoji.Simple("afraid", Seq("afraid"), "(ㆆ _ ㆆ)"),
    Asciimoji.Simple("alpha", Seq("alpha"), "α"),
    Asciimoji.Simple("angel", Seq("angel"), "☜(⌒▽⌒)☞"),
    Asciimoji.Simple("angry", Seq("angry"), "•`_´•"),
    Asciimoji.Simple("arrowhead", Seq("arrowhead"), "⤜(ⱺ ʖ̯ⱺ)⤏"),
    Asciimoji.Simple("apple", Seq("apple"), ""),
    Asciimoji.Simple("ass", Seq("ass", "butt"), "(‿|‿)"),
    Asciimoji.Simple("awkward", Seq("awkward"), "•͡˘㇁•͡˘"),
    Asciimoji.Simple("bat", Seq("bat"), "/|\\ ^._.^ /|\\"),
    Asciimoji.Simple("bear", Seq("bear", "koala"), "ʕ·͡ᴥ·ʔ﻿"),
    Asciimoji.Simple("bearflip", Seq("bearflip"), "ʕノ•ᴥ•ʔノ ︵ ┻━┻"),
    Asciimoji.Simple("bearhug", Seq("bearhug"), "ʕっ•ᴥ•ʔっ"),
    Asciimoji.Simple("because", Seq("because", "since"), "∵"),
    Asciimoji.Simple("beta", Seq("beta"), "β"),
    Asciimoji.Simple("bigheart", Seq("bigheart"), "❤"),
    Asciimoji.Simple("blackeye", Seq("blackeye"), "0__#"),
    Asciimoji.Simple("blubby", Seq("blubby"), "(      0    _   0    )"),
    Asciimoji.Simple("blush", Seq("blush"), "(˵ ͡° ͜ʖ ͡°˵)"),
    Asciimoji.Simple("bond", Seq("bond", "007"), "┌( ͝° ͜ʖ͡°)=ε/̵͇̿̿/’̿’̿ ̿"),
    Asciimoji.Simple("boobs", Seq("boobs"), "( . Y . )"),
    Asciimoji.Simple("bored", Seq("bored"), "(-_-)"),
    Asciimoji.Simple("bribe", Seq("bribe"), "( •͡˘ _•͡˘)ノð"),
    Asciimoji.Simple("bubbles", Seq("bubbles"), "( ˘ ³˘)ノ°ﾟº❍｡"),
    Asciimoji.Simple("butterfly", Seq("butterfly"), "ƸӜƷ"),
    Asciimoji.Simple("cat", Seq("cat"), "(= ФェФ=)"),
    Asciimoji.Simple("catlenny", Seq("catlenny"), "( ͡° ᴥ ͡°)﻿"),
    Asciimoji.Simple("check", Seq("check"), "✔"),
    Asciimoji.Simple("cheer", Seq("cheer"), "※\\(^o^)/※"),
    Asciimoji.Simple("chubby", Seq("chubby"), "╭(ʘ̆~◞౪◟~ʘ̆)╮"),
    Asciimoji.Simple("claro", Seq("claro"), "(͡ ° ͜ʖ ͡ °)"),
    Asciimoji.Simple(
      "clique",
      Seq("clique", "gang", "squad"),
      "ヽ༼ ຈل͜ຈ༼ ▀̿̿Ĺ̯̿̿▀̿ ̿༽Ɵ͆ل͜Ɵ͆ ༽ﾉ"
    ),
    Asciimoji.Simple("cloud", Seq("cloud"), "☁"),
    Asciimoji.Simple("club", Seq("club"), "♣"),
    Asciimoji.Simple("coffee", Seq("coffee", "cuppa"), "c[_]"),
    Asciimoji.Simple("cmd", Seq("cmd", "command"), "⌘"),
    Asciimoji.Simple("cool", Seq("cool", "csi"), "(•_•) ( •_•)>⌐■-■ (⌐■_■)"),
    Asciimoji.Simple("copy", Seq("copy", "c"), "©"),
    Asciimoji.Simple("creep", Seq("creep"), "ԅ(≖‿≖ԅ)"),
    Asciimoji.Simple("creepcute", Seq("creepcute"), "ƪ(ړײ)‎ƪ​​"),
    Asciimoji.Simple("crim3s", Seq("crim3s"), "( ✜︵✜ )"),
    Asciimoji.Simple("cross", Seq("cross"), "†"),
    Asciimoji.Simple("cry", Seq("cry"), "(╥﹏╥)"),
    Asciimoji.Simple("crywave", Seq("crywave"), "( ╥﹏╥) ノシ"),
    Asciimoji.Simple("cute", Seq("cute"), "(｡◕‿‿◕｡)"),
    Asciimoji.Simple("d1", Seq("d1"), "⚀"),
    Asciimoji.Simple("d2", Seq("d2"), "⚁"),
    Asciimoji.Simple("d3", Seq("d3"), "⚂"),
    Asciimoji.Simple("d4", Seq("d4"), "⚃"),
    Asciimoji.Simple("d5", Seq("d5"), "⚄"),
    Asciimoji.Simple("d6", Seq("d6"), "⚅"),
    Asciimoji.Simple("dab", Seq("dab"), "ヽ( •_)ᕗ"),
    Asciimoji.Simple("damnyou", Seq("damnyou"), "(ᕗ ͠° ਊ ͠° )ᕗ"),
    Asciimoji.Simple("dance", Seq("dance"), "ᕕ(⌐■_■)ᕗ ♪♬"),
    Asciimoji.Simple("dead", Seq("dead"), "x⸑x"),
    Asciimoji.Simple("dealwithit", Seq("dealwithit", "dwi"), "(⌐■_■)"),
    Asciimoji.Simple("delta", Seq("delta"), "Δ"),
    Asciimoji.Simple("depressed", Seq("depressed"), "(︶︹︶)"),
    Asciimoji.Simple("derp", Seq("derp"), "☉ ‿ ⚆"),
    Asciimoji.Simple("diamond", Seq("diamond"), "♦"),
    Asciimoji.Simple("dj", Seq("dj"), "d[-_-]b"),
    Asciimoji.Simple("dog", Seq("dog"), "(◕ᴥ◕ʋ)"),
    Asciimoji.Simple("dollar", Seq("dollar"), "$"),
    Asciimoji.Parameterized(
      "dollarbill",
      Seq("dollarbill", "$"),
      "20",
      maybeAmount => {
        val amount = maybeAmount.getOrElse("10").flatMap {
          case '0'   => "ο̲̅"
          case '1'   => "̅ι"
          case '2'   => "2̅"
          case '3'   => "3̅"
          case '4'   => "4̅"
          case '5'   => "5̲̅"
          case '6'   => "6̅"
          case '7'   => "7̅"
          case '8'   => "8̅"
          case '9'   => "9̅"
          case other => String.valueOf(other)
        }
        "[̲̅$̲̅(̲" + amount + "̅)̲̅$̲̅]"
      }
    ),
    Asciimoji.Simple("dong", Seq("dong"), "(̿▀̿ ̿Ĺ̯̿̿▀̿ ̿)̄"),
    Asciimoji.Simple("donger", Seq("donger"), "ヽ༼ຈل͜ຈ༽ﾉ"),
    Asciimoji.Simple("dontcare", Seq("dontcare", "idc"), "(- ʖ̯-)"),
    Asciimoji.Simple("do not want", Seq("do not want", "dontwant"), "ヽ(｀Д´)ﾉ"),
    Asciimoji.Simple("dope", Seq("dope"), "<(^_^)>"),
    Asciimoji.Simple("<<", Seq("<<"), "«"),
    Asciimoji.Simple(">>", Seq(">>"), "»"),
    Asciimoji.Simple("doubleflat", Seq("doubleflat"), "𝄫"),
    Asciimoji.Simple("doublesharp", Seq("doublesharp"), "𝄪"),
    Asciimoji
      .Simple("doubletableflip", Seq("doubletableflip"), "┻━┻ ︵ヽ(`Д´)ﾉ︵ ┻━┻"),
    Asciimoji.Simple("down", Seq("down"), "↓"),
    Asciimoji.Simple("duckface", Seq("duckface"), "(・3・)"),
    Asciimoji.Simple("duel", Seq("duel"), "ᕕ(╭ರ╭ ͟ʖ╮•́)⊃¤=(————-"),
    Asciimoji.Simple("duh", Seq("duh"), "(≧︿≦)"),
    Asciimoji.Simple("dunno", Seq("dunno"), "¯\\(°_o)/¯"),
    Asciimoji.Simple("ebola", Seq("ebola"), "ᴇʙᴏʟᴀ"),
    Asciimoji
      .Simple("eeriemob", Seq("eeriemob"), "(-(-_-(-_(-_(-_-)_-)-_-)_-)_-)-)"),
    Asciimoji.Simple("ellipsis", Seq("ellipsis", "..."), "…"),
    Asciimoji.Simple("emdash", Seq("emdash", "--"), "–"),
    Asciimoji.Simple("emptystar", Seq("emptystar"), "☆"),
    Asciimoji.Simple("emptytriangle", Seq("emptytriangle", "t2"), "△"),
    Asciimoji.Simple("endure", Seq("endure"), "(҂◡_◡) ᕤ"),
    Asciimoji.Simple("envelope", Seq("envelope", "letter"), "✉︎"),
    Asciimoji.Simple("epsilon", Seq("epsilon"), "ɛ"),
    Asciimoji.Simple("euro", Seq("euro"), "€"),
    Asciimoji.Simple("evil", Seq("evil"), "ψ(｀∇´)ψ"),
    Asciimoji.Simple("evillenny", Seq("evillenny"), "(͠≖ ͜ʖ͠≖)"),
    Asciimoji.Simple("excited", Seq("excited"), "(ﾉ◕ヮ◕)ﾉ*:・ﾟ✧"),
    Asciimoji.Simple("execution", Seq("execution"), "(⌐■_■)︻╦╤─   (╥﹏╥)"),
    Asciimoji.Simple("facebook", Seq("facebook"), "(╯°□°)╯︵ ʞooqǝɔɐɟ"),
    Asciimoji.Simple("facepalm", Seq("facepalm"), "(－‸ლ)"),
    Asciimoji.Parameterized(
      "fancytext",
      Seq("fancytext"),
      "beware, i am fancy!",
      _.getOrElse("beware, i am fancy!").map {
        case 'a'   => 'α'
        case 'b'   => 'в'
        case 'c'   => '¢'
        case 'd'   => '∂'
        case 'e'   => 'є'
        case 'f'   => 'ƒ'
        case 'g'   => 'g'
        case 'h'   => 'н'
        case 'i'   => 'ι'
        case 'j'   => 'נ'
        case 'k'   => 'к'
        case 'l'   => 'ℓ'
        case 'm'   => 'м'
        case 'n'   => 'η'
        case 'o'   => 'σ'
        case 'p'   => 'ρ'
        case 'q'   => 'q'
        case 'r'   => 'я'
        case 's'   => 'ѕ'
        case 't'   => 'т'
        case 'u'   => 'υ'
        case 'v'   => 'ν'
        case 'w'   => 'ω'
        case 'x'   => 'χ'
        case 'y'   => 'у'
        case 'z'   => 'z'
        case other => other
      }
    ),
    Asciimoji.Simple("fart", Seq("fart"), "(ˆ⺫ˆ๑)<3"),
    Asciimoji.Simple("fight", Seq("fight"), "(ง •̀_•́)ง"),
    Asciimoji.Simple("finn", Seq("finn"), "| (• ◡•)|"),
    Asciimoji.Simple("fish", Seq("fish"), """<"(((<3"""),
    Asciimoji.Simple("5", Seq("5", "five"), "卌"),
    Asciimoji.Simple("5/8", Seq("5/8"), "⅝"),
    Asciimoji.Simple("flat", Seq("flat", "bemolle"), "♭"),
    Asciimoji.Simple("flexing", Seq("flexing"), "ᕙ(`▽´)ᕗ"),
    Asciimoji.Parameterized(
      "fliptext",
      Seq("fliptext"),
      "flip me like a table",
      fliptext
    ),
    Asciimoji.Parameterized(
      "fliptexttable",
      Seq("fliptexttable"),
      "flip me like a table",
      maybeStr => "(ノ ゜Д゜)ノ ︵  " + fliptext(maybeStr)
    ),
    Asciimoji
      .Simple("flipped", Seq("flipped", "heavytable"), "┬─┬﻿ ︵ /(.□. \\）"),
    Asciimoji.Simple("flower", Seq("flower", "flor"), "(✿◠‿◠)"),
    Asciimoji.Simple("f", Seq("f"), "✿"),
    Asciimoji.Simple("fly", Seq("fly"), "─=≡Σ((( つ◕ل͜◕)つ"),
    Asciimoji
      .Simple("friendflip", Seq("friendflip"), "(╯°□°)╯︵ ┻━┻ ︵ ╯(°□° ╯)"),
    Asciimoji.Simple("frown", Seq("frown"), "(ღ˘⌣˘ღ)"),
    Asciimoji.Simple("fuckoff", Seq("fuckoff", "gtfo"), "୧༼ಠ益ಠ╭∩╮༽"),
    Asciimoji.Simple("fuckyou", Seq("fuckyou", "fu"), "┌П┐(ಠ_ಠ)"),
    Asciimoji.Simple("gentleman", Seq("gentleman", "sir", "monocle"), "ಠ_ರೃ"),
    Asciimoji.Simple("ghast", Seq("ghast"), "= _ ="),
    Asciimoji.Simple("ghost", Seq("ghost"), "༼ つ ╹ ╹ ༽つ"),
    Asciimoji.Simple("gift", Seq("gift", "present"), "(´・ω・)っ由"),
    Asciimoji.Simple("gimme", Seq("gimme"), "༼ つ ◕_◕ ༽つ"),
    Asciimoji.Simple("givemeyourmoney", Seq("givemeyourmoney"), "(•-•)⌐"),
    Asciimoji.Simple("glitter", Seq("glitter"), "(*・‿・)ノ⌒*:･ﾟ✧"),
    Asciimoji.Simple("glasses", Seq("glasses"), "(⌐ ͡■ ͜ʖ ͡■)"),
    Asciimoji.Simple("glassesoff", Seq("glassesoff"), "( ͡° ͜ʖ ͡°)ﾉ⌐■-■"),
    Asciimoji.Simple("glitterderp", Seq("glitterderp"), "(ﾉ☉ヮ⚆)ﾉ ⌒*:･ﾟ✧"),
    Asciimoji.Simple("gloomy", Seq("gloomy"), "(_゜_゜_)"),
    Asciimoji.Simple("goatse", Seq("goatse"), "(з๏ε)"),
    Asciimoji.Simple("gotit", Seq("gotit"), "(☞ﾟ∀ﾟ)☞"),
    Asciimoji.Simple("greet", Seq("greet", "greetings"), "( ´◔ ω◔`) ノシ"),
    Asciimoji.Simple("gun", Seq("gun", "mg"), "︻╦╤─"),
    Asciimoji.Simple("hadouken", Seq("hadouken"), "༼つಠ益ಠ༽つ ─=≡ΣO))"),
    Asciimoji.Simple("hammerandsickle", Seq("hammerandsickle", "hs"), "☭"),
    Asciimoji.Simple("handleft", Seq("handleft", "hl"), "☜"),
    Asciimoji.Simple("handright", Seq("handright", "hr"), "☞"),
    Asciimoji.Simple("haha", Seq("haha"), "٩(^‿^)۶"),
    Asciimoji.Simple("happy", Seq("happy"), "٩( ๑╹ ꇴ╹)۶"),
    Asciimoji.Simple("happygarry", Seq("happygarry"), "ᕕ( ᐛ )ᕗ"),
    Asciimoji.Simple("h", Seq("h", "heart"), "♥"),
    Asciimoji.Simple("hello", Seq("hello", "ohai", "bye"), "(ʘ‿ʘ)╯"),
    Asciimoji.Simple("help", Seq("help"), "\\(°Ω°)/"),
    Asciimoji.Simple("highfive", Seq("highfive"), "._.)/\\(._."),
    Asciimoji.Simple("hitting", Seq("hitting"), "( ｀皿´)｡ﾐ/"),
    Asciimoji.Simple("hug", Seq("hug", "hugs"), "(づ｡◕‿‿◕｡)づ"),
    Asciimoji.Simple("iknowright", Seq("iknowright", "ikr"), "┐｜･ิω･ิ#｜┌"),
    Asciimoji.Simple("illuminati", Seq("illuminati"), "୧(▲ᴗ▲)ノ"),
    Asciimoji.Simple("infinity", Seq("infinity", "inf"), "∞"),
    Asciimoji.Simple("inlove", Seq("inlove"), "(っ´ω`c)♡"),
    Asciimoji.Simple("int", Seq("int"), "∫"),
    Asciimoji.Simple("internet", Seq("internet"), "ଘ(੭*ˊᵕˋ)੭* ̀ˋ ɪɴᴛᴇʀɴᴇᴛ"),
    Asciimoji.Simple("interrobang", Seq("interrobang"), "‽"),
    Asciimoji.Simple("jake", Seq("jake"), "(❍ᴥ❍ʋ)"),
    Asciimoji.Simple("kappa", Seq("kappa"), "(¬,‿,¬)"),
    Asciimoji.Simple("kawaii", Seq("kawaii"), "≧◡≦"),
    Asciimoji.Simple("keen", Seq("keen"), "┬┴┬┴┤Ɵ͆ل͜Ɵ͆ ༽ﾉ"),
    Asciimoji.Simple("kiahh", Seq("kiahh"), "~\\(≧▽≦)/~"),
    Asciimoji.Simple("kiss", Seq("kiss"), "(づ ￣ ³￣)づ"),
    Asciimoji.Simple("kyubey", Seq("kyubey"), "／人◕ ‿‿ ◕人＼"),
    Asciimoji.Simple("lambda", Seq("lambda"), "λ"),
    Asciimoji.Simple("lazy", Seq("lazy"), "_(:3」∠)_"),
    Asciimoji.Simple("left", Seq("left", "<-"), "←"),
    Asciimoji.Simple("lenny", Seq("lenny"), "( ͡° ͜ʖ ͡°)"),
    Asciimoji
      .Simple("lennybill", Seq("lennybill"), "[̲̅$̲̅(̲̅ ͡° ͜ʖ ͡°̲̅)̲̅$̲̅]"),
    Asciimoji.Simple("lennyfight", Seq("lennyfight"), "(ง ͠° ͟ʖ ͡°)ง"),
    Asciimoji
      .Simple("lennyflip", Seq("lennyflip"), "(ノ ͡° ͜ʖ ͡°ノ)   ︵ ( ͜。 ͡ʖ ͜。)"),
    Asciimoji
      .Simple("lennygang", Seq("lennygang"), "( ͡°( ͡° ͜ʖ( ͡° ͜ʖ ͡°)ʖ ͡°) ͡°)"),
    Asciimoji.Simple("lennyshrug", Seq("lennyshrug"), "¯\\_( ͡° ͜ʖ ͡°)_/¯"),
    Asciimoji.Simple("lennysir", Seq("lennysir"), "( ಠ ͜ʖ ರೃ)"),
    Asciimoji.Simple("lennystalker", Seq("lennystalker"), "┬┴┬┴┤( ͡° ͜ʖ├┬┴┬┴"),
    Asciimoji.Simple("lennystrong", Seq("lennystrong"), "ᕦ( ͡° ͜ʖ ͡°)ᕤ"),
    Asciimoji
      .Simple("lennywizard", Seq("lennywizard"), "╰( ͡° ͜ʖ ͡° )つ──☆*:・ﾟ"),
    Asciimoji.Parameterized(
      "loading",
      Seq("loading"),
      "30",
      maybeStr => {
        val percent =
          (for {
            str <- maybeStr
            number <- Try(str.toInt).toOption
            if 0 <= number && number <= 100
          } yield number).getOrElse(30).toDouble
        val filledBlocks =
          if (Math.round(percent / 10) > 10) 10
          else Math.round(percent / 10).toInt
        val emptyBlocks = 10 - filledBlocks
        ("█" * filledBlocks) + ("▒" * emptyBlocks)
      }
    ),
    Asciimoji.Simple("lol", Seq("lol"), "L(° O °L)"),
    Asciimoji.Simple("look", Seq("look"), "(ಡ_ಡ)☞"),
    Asciimoji.Simple("loud", Seq("loud", "noise"), "ᕦ(⩾﹏⩽)ᕥ"),
    Asciimoji.Simple("love", Seq("love"), "♥‿♥"),
    Asciimoji.Simple("lovebear", Seq("lovebear"), "ʕ♥ᴥ♥ʔ"),
    Asciimoji.Simple("lumpy", Seq("lumpy"), "꒰ ꒡⌓꒡꒱"),
    Asciimoji.Simple("luv", Seq("luv"), "-`ღ´-"),
    Asciimoji.Simple("magic", Seq("magic"), "ヽ(｀Д´)⊃━☆ﾟ. * ･ ｡ﾟ,"),
    Asciimoji.Simple("magicflip", Seq("magicflip"), "(/¯◡ ‿ ◡)/¯ ~ ┻━┻"),
    Asciimoji.Simple("meep", Seq("meep"), "\\(°^°)/"),
    Asciimoji.Simple("meh", Seq("meh"), "ಠ_ಠ"),
    Asciimoji.Simple("metal", Seq("metal", "rock"), "\\m/,(> . <)_\\m/"),
    Asciimoji.Simple("mistyeyes", Seq("mistyeyes"), "ಡ_ಡ"),
    Asciimoji.Simple("monster", Seq("monster"), "༼ ༎ຶ ෴ ༎ຶ༽"),
    Asciimoji.Simple("natural", Seq("natural"), "♮"),
    Asciimoji.Simple("needle", Seq("needle", "inject"), "┌(◉ ͜ʖ◉)つ┣▇▇▇═──"),
    Asciimoji.Simple("nerd", Seq("nerd"), "(⌐⊙_⊙)"),
    Asciimoji.Simple("nice", Seq("nice"), "( ͡° ͜ °)"),
    Asciimoji.Simple("no", Seq("no"), "→_←"),
    Asciimoji.Simple("noclue", Seq("noclue"), "／人◕ __ ◕人＼"),
    Asciimoji.Simple("nom", Seq("nom", "yummy", "delicious"), "(っˆڡˆς)"),
    Asciimoji.Simple("note", Seq("note", "sing"), "♫"),
    Asciimoji.Simple("nuclear", Seq("nuclear", "radioactive", "nukular"), "☢"),
    Asciimoji.Simple("nyan", Seq("nyan"), "~=[,,_,,]:3"),
    Asciimoji.Simple("nyeh", Seq("nyeh"), "@^@"),
    Asciimoji.Simple("ohshit", Seq("ohshit"), "( º﹃º )"),
    Asciimoji.Simple("omega", Seq("omega"), "Ω"),
    Asciimoji.Simple("omg", Seq("omg"), "◕_◕"),
    Asciimoji.Simple("1/6", Seq("1/6"), "⅛"),
    Asciimoji.Simple("1/4", Seq("1/4"), "¼"),
    Asciimoji.Simple("1/2", Seq("1/2"), "½"),
    Asciimoji.Simple("1/3", Seq("1/3"), "⅓"),
    Asciimoji.Simple("opt", Seq("opt", "option"), "⌥"),
    Asciimoji.Simple("orly", Seq("orly"), "(눈_눈)"),
    Asciimoji.Simple("ohyou", Seq("ohyou", "ou"), "(◞థ౪థ)ᴖ"),
    Asciimoji.Simple("peace", Seq("peace"), "✌(-‿-)✌"),
    Asciimoji.Simple("pear", Seq("pear"), "(__>-"),
    Asciimoji.Simple("pi", Seq("pi"), "π"),
    Asciimoji.Simple("pingpong", Seq("pingpong"), "( •_•)O*¯`·.¸.·´¯`°Q(•_• )"),
    Asciimoji.Simple("plain", Seq("plain"), "._."),
    Asciimoji.Simple("pleased", Seq("pleased"), "(˶‾᷄ ⁻̫ ‾᷅˵)"),
    Asciimoji.Simple("point", Seq("point"), "(☞ﾟヮﾟ)☞"),
    Asciimoji.Simple("pooh", Seq("pooh"), "ʕ •́؈•̀)"),
    Asciimoji.Simple("porcupine", Seq("porcupine"), "(•ᴥ• )́`́'́`́'́⻍"),
    Asciimoji.Simple("pound", Seq("pound"), "£"),
    Asciimoji.Simple("praise", Seq("praise"), "(☝ ՞ਊ ՞)☝"),
    Asciimoji.Simple("punch", Seq("punch"), "O=('-'Q)"),
    Asciimoji.Simple("rage", Seq("rage", "mad"), "t(ಠ益ಠt)"),
    Asciimoji.Simple("rageflip", Seq("rageflip"), "(ノಠ益ಠ)ノ彡┻━┻"),
    Asciimoji.Simple("rainbowcat", Seq("rainbowcat"), "(=^･ｪ･^=))ﾉ彡☆"),
    Asciimoji.Simple("really", Seq("really"), "ò_ô"),
    Asciimoji.Simple("r", Seq("r"), "®"),
    Asciimoji.Simple("right", Seq("right", "->"), "→"),
    Asciimoji.Simple("riot", Seq("riot"), "୧༼ಠ益ಠ༽୨"),
    Asciimoji.Parameterized(
      "rolldice",
      Seq("rolldice"),
      "2",
      maybeStr => {
        val amount =
          (for {
            str <- maybeStr
            number <- Try(str.toInt).toOption
            if 0 < number && number < 100 // Let's add a sensible max limit...
          } yield number).getOrElse(1)
        val dice = Seq("⚀", "⚁", "⚂", "⚃", "⚄", "⚅")
        (1 to amount).map(_ => dice(Random.nextInt(6))).mkString(" ")
      }
    ),
    Asciimoji.Simple("rolleyes", Seq("rolleyes"), "(◔_◔)"),
    Asciimoji.Simple("rose", Seq("rose"), "✿ڿڰۣ—"),
    Asciimoji.Simple("run", Seq("run"), "(╯°□°)╯"),
    Asciimoji.Simple("sad", Seq("sad"), "ε(´סּ︵סּ`)з"),
    Asciimoji.Simple("saddonger", Seq("saddonger"), "ヽ༼ຈʖ̯ຈ༽ﾉ"),
    Asciimoji.Simple("sadlenny", Seq("sadlenny"), "( ͡° ʖ̯ ͡°)"),
    Asciimoji.Simple("7/8", Seq("7/8"), "⅞"),
    Asciimoji.Simple("sharp", Seq("sharp", "diesis"), "♯"),
    Asciimoji.Simple("shout", Seq("shout"), "╚(•⌂•)╝"),
    Asciimoji.Simple("shrug", Seq("shrug"), "¯\\_(ツ)_/¯"),
    Asciimoji.Simple("shy", Seq("shy"), "=^_^="),
    Asciimoji.Simple("sigma", Seq("sigma"), "Σ"),
    Asciimoji.Simple("skull", Seq("skull"), "☠"),
    Asciimoji.Simple("smile", Seq("smile"), "ツ"),
    Asciimoji.Simple("smiley", Seq("smiley"), "☺︎"),
    Asciimoji.Simple("smirk", Seq("smirk"), "¬‿¬"),
    Asciimoji.Simple("snowman", Seq("snowman"), "☃"),
    Asciimoji.Simple("sob", Seq("sob"), "(;´༎ຶД༎ຶ`)"),
    Asciimoji
      .Simple("soviettableflip", Seq("soviettableflip"), "ノ┬─┬ノ ︵ ( \\o°o)\\"),
    Asciimoji.Simple("spade", Seq("spade"), "♠"),
    Asciimoji.Simple("sqrt", Seq("sqrt"), "√"),
    Asciimoji.Simple("squid", Seq("squid"), "<コ:彡"),
    Asciimoji.Simple("star", Seq("star"), "★"),
    Asciimoji.Simple("strong", Seq("strong"), "ᕙ(⇀‸↼‶)ᕗ"),
    Asciimoji.Simple("suicide", Seq("suicide"), "ε/̵͇̿̿/’̿’̿ ̿(◡︵◡)"),
    Asciimoji.Simple("sum", Seq("sum"), "∑"),
    Asciimoji.Simple("sun", Seq("sun"), "☀"),
    Asciimoji.Simple("surprised", Seq("surprised"), "(๑•́ ヮ •̀๑)"),
    Asciimoji.Simple("surrender", Seq("surrender"), "\\_(-_-)_/"),
    Asciimoji.Simple("stalker", Seq("stalker"), "┬┴┬┴┤(･_├┬┴┬┴"),
    Asciimoji.Simple("swag", Seq("swag"), "(̿▀̿‿ ̿▀̿ ̿)"),
    Asciimoji.Simple("sword", Seq("sword"), "o()xxxx[{::::::::::::::::::>"),
    Asciimoji.Simple("tabledown", Seq("tabledown"), "┬─┬﻿ ノ( ゜-゜ノ)"),
    Asciimoji.Simple("tableflip", Seq("tableflip"), "(ノ ゜Д゜)ノ ︵ ┻━┻"),
    Asciimoji.Simple("tau", Seq("tau"), "τ"),
    Asciimoji.Simple("tears", Seq("tears"), "(ಥ﹏ಥ)"),
    Asciimoji.Simple("terrorist", Seq("terrorist"), "୧༼ಠ益ಠ༽︻╦╤─"),
    Asciimoji.Simple("thanks", Seq("thanks", "thankyou", "ty"), "\\(^-^)/"),
    Asciimoji.Simple("therefore", Seq("therefore", "so"), "⸫"),
    Asciimoji.Simple("this", Seq("this"), "( ͡° ͜ʖ ͡°)_/¯"),
    Asciimoji.Simple("3/8", Seq("3/8"), "⅜"),
    Asciimoji.Simple("tiefighter", Seq("tiefighter"), "|=-(¤)-=|"),
    Asciimoji.Simple("tired", Seq("tired"), "(=____=)"),
    Asciimoji.Simple("toldyouso", Seq("toldyouso", "toldyou"), "☜(꒡⌓꒡)"),
    Asciimoji.Simple("toogood", Seq("toogood"), "ᕦ(òᴥó)ᕥ"),
    Asciimoji.Simple("tm", Seq("tm"), "™"),
    Asciimoji.Simple("triangle", Seq("triangle", "t"), "▲"),
    Asciimoji.Simple("2/3", Seq("2/3"), "⅔"),
    Asciimoji.Simple("unflip", Seq("unflip"), "┬──┬ ノ(ò_óノ)"),
    Asciimoji.Simple("up", Seq("up"), "↑"),
    Asciimoji.Simple("victory", Seq("victory"), "(๑•̀ㅂ•́)ง✧"),
    Asciimoji.Simple("wat", Seq("wat"), "(ÒДÓױ)"),
    Asciimoji.Simple("wave", Seq("wave"), "( * ^ *) ノシ"),
    Asciimoji.Simple("whaa", Seq("whaa"), "Ö"),
    Asciimoji.Simple("whistle", Seq("whistle"), "(っ^з^)♪♬"),
    Asciimoji.Simple("whoa", Seq("whoa"), "(°o•)"),
    Asciimoji.Simple("why", Seq("why"), "ლ(`◉◞౪◟◉‵ლ)"),
    Asciimoji.Parameterized(
      "witchtext",
      Seq("witchtext"),
      "when shall we three meet again?",
      _.getOrElse("when shall we three meet again?").map {
        case 'a'   => 'Λ'
        case 'b'   => 'ß'
        case 'c'   => '¢'
        case 'd'   => 'Ð'
        case 'e'   => 'Σ'
        case 'f'   => 'Ŧ'
        case 'g'   => 'G'
        case 'h'   => 'H'
        case 'i'   => '|'
        case 'j'   => '⅃'
        case 'k'   => 'Ҡ'
        case 'l'   => 'L'
        case 'm'   => 'M'
        case 'n'   => 'И'
        case 'o'   => 'Ө'
        case 'p'   => 'þ'
        case 'q'   => 'Q'
        case 'r'   => 'Я'
        case 's'   => '$'
        case 't'   => '†'
        case 'u'   => 'V'
        case 'v'   => 'V'
        case 'w'   => 'W'
        case 'x'   => 'X'
        case 'y'   => 'Ұ'
        case 'z'   => 'Z'
        case other => other
      }
    ),
    Asciimoji.Simple("woo", Seq("woo"), "＼(＾O＾)／"),
    Asciimoji.Simple("wtf", Seq("wtf"), "(⊙＿⊙')"),
    Asciimoji.Simple("wut", Seq("wut"), "⊙ω⊙"),
    Asciimoji.Simple("yay", Seq("yay"), "\\( ﾟヮﾟ)/"),
    Asciimoji.Simple("yeah", Seq("yeah", "yes"), "(•̀ᴗ•́)و ̑̑"),
    Asciimoji.Simple("yen", Seq("yen"), "¥"),
    Asciimoji.Simple("yinyang", Seq("yinyang", "yy"), "☯"),
    Asciimoji.Simple("yolo", Seq("yolo"), "Yᵒᵘ Oᶰˡʸ Lᶤᵛᵉ Oᶰᶜᵉ"),
    Asciimoji.Simple("youkids", Seq("youkids", "ukids"), "ლ༼>╭ ͟ʖ╮<༽ლ"),
    Asciimoji.Simple("y u no", Seq("y u no", "yuno"), "(屮ﾟДﾟ)屮 Y U NO"),
    Asciimoji.Simple("zen", Seq("zen", "meditation", "omm"), "⊹╰(⌣ʟ⌣)╯⊹"),
    Asciimoji.Simple("zoidberg", Seq("zoidberg"), "(V) (°,,,,°) (V)"),
    Asciimoji.Simple("zombie", Seq("zombie"), "[¬º-°]¬")
  )

  def samples: Seq[(Asciimoji, String)] = asciimojis.map {
    case s: Asciimoji.Simple        => (s, s.ascii)
    case p: Asciimoji.Parameterized => (p, p.render(Some(p.exampleParam)))
  }

  private val dictionnary: Map[String, Asciimoji] =
    asciimojis.flatMap { asciimoji =>
      asciimoji.words.map(word => (word, asciimoji))
    }.toMap

  private def fliptext(maybeStr: Option[String]) =
    maybeStr.getOrElse("flip me like a table").map {
      case 'a'      => '\u0250'
      case 'b'      => 'q'
      case 'c'      => '\u0254'
      case 'd'      => 'p'
      case 'e'      => '\u01DD'
      case 'f'      => '\u025F'
      case 'g'      => '\u0183'
      case 'h'      => '\u0265'
      case 'i'      => '\u0131'
      case 'j'      => '\u027E'
      case 'k'      => '\u029E'
      case 'l'      => '\u05DF'
      case 'm'      => '\u026F'
      case 'n'      => 'u'
      case 'p'      => 'd'
      case 'q'      => 'b'
      case 'r'      => '\u0279'
      case 't'      => '\u0287'
      case 'u'      => 'n'
      case 'v'      => '\u028C'
      case 'w'      => '\u028D'
      case 'y'      => '\u028E'
      case '.'      => '\u02D9'
      case '['      => ']'
      case '('      => ')'
      case '{'      => '}'
      case '?'      => '\u00BF'
      case '!'      => '\u00A1'
      case '\''     => ','
      case '<'      => '>'
      case '_'      => '\u203E'
      case '"'      => '\u201E'
      case '\\'     => '\\'
      case ';'      => '\u061B'
      case '\u203F' => '\u2040'
      case '\u2045' => '\u2046'
      case '\u2234' => '\u2235'
      case other    => other
    }
}
