package com.innovenso.townplan.io.model

import com.innovenso.townplanner.model.meta.Color

case class ColorScheme(
    textColor: Color = Color(37, 26, 6),
    highlightColor: Color = Color(8, 136, 140),
    linkColor: Color = Color(140, 131, 79),
    headerColor: Color = Color(115, 81, 18),
    backgroundColor: Color = Color(222, 212, 196),
    borderColor: Color = Color(77, 77, 77),
    redColor: Color = Color(255, 0, 0),
    greenColor: Color = Color(0, 255, 0),
    amberColor: Color = Color(255, 255, 0),
    blackColor: Color = Color(0, 0, 0),
    whiteColor: Color = Color(255, 255, 255),
    accentColors: List[Color] = List(
      Color(140, 131, 79),
      Color(8, 156, 61),
      Color(141, 24, 245),
      Color(59, 121, 235),
      Color(150, 156, 39),
      Color(158, 127, 51),
      Color(98, 51, 235),
      Color(104, 158, 19),
      Color(235, 79, 49),
      Color(245, 33, 24),
      Color(47, 87, 158)
    )
) {
  def accentColor(number: Int): Color =
    if (accentColors.isEmpty) highlightColor
    else
      accentColors(number % accentColors.length)

  val numberOfAccentColors: Int = 20

  def toMap: Map[String, Color] = Map(
    ("innovensoWhite", whiteColor),
    ("innovensoBlack", blackColor),
    ("innovensoGreen", greenColor),
    ("innovensoAmber", amberColor),
    ("innovensoRed", redColor),
    ("innovensoBorder", borderColor),
    ("innovensoBackground", backgroundColor),
    ("innovensoHeader", headerColor),
    ("innovensoText", textColor),
    ("innovensoHighLight", highlightColor),
    ("innovensoLink", linkColor)
  ) ++ (1 to numberOfAccentColors).map(index =>
    ("innovensoAccent" + index, accentColor(index))
  )
}
