package com.innovenso.townplanner.io.latex

object LatexFormat {

  def escape(text: String): String = {
    var i = 0
    val builder: StringBuilder = new StringBuilder
    while (i < text.length) {
      text.charAt(i) match {
        case '\\' => builder.append("\\textbackslash ")
        case 'ë'  => builder.append("\\\"e")
        case 'é'  => builder.append("\\\'e")
        case 'è'  => builder.append("\\`e")
        case 'ê'  => builder.append("\\^e")
        case 'ü'  => builder.append("\\\"u")
        case 'û'  => builder.append("\\^u")
        case 'ä'  => builder.append("\\\"a")
        case 'à'  => builder.append("\\`a")
        case 'â'  => builder.append("\\^a")
        case 'ö'  => builder.append("\\\"o")
        case 'ô'  => builder.append("\\^o")
        case 'ï'  => builder.append("\\\"i")
        case 'î'  => builder.append("\\^i")
        case '&'  => builder.append("\\&")
        case '%'  => builder.append("\\%")
        case '$'  => builder.append("\\$")
        case '#'  => builder.append("\\#")
        case '_'  => builder.append("\\_")
        case '{'  => builder.append("\\{")
        case '}'  => builder.append("\\}")
        case '~'  => builder.append("\\textasciitilde ")
        case '^'  => builder.append("\\textasciicircum ")
        case '<'  => builder.append("\\textless ")
        case '>'  => builder.append("\\textgreater ")
        case '€'  => builder.append("\\texteuro ")
        case '£'  => builder.append("\\textpounds ")
        case '|'  => builder.append("\\textbar ")
        case '-'  => builder.append("\\textendash ")
        case '~'  => builder.append("\\textasciitilde{} ")
        case 'ç'  => builder.append("\\c{c}")

        case c => builder += c
      }
      i += 1
    }
    builder.toString()
  }
}
