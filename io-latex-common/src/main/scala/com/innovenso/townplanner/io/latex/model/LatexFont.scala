package com.innovenso.townplanner.io.latex.model

trait LatexFont {
  def name: String
}

case object Tiny extends LatexFont {
  val name = "\\tiny"
}

case object Script extends LatexFont {
  val name = "\\scriptsize"
}

case object Footnote extends LatexFont {
  val name = "\\footnotesize"
}

case object Small extends LatexFont {
  val name = "\\small"
}

case object Normal extends LatexFont {
  val name = "\\normalsize"
}

case object Large extends LatexFont {
  val name = "\\large"
}

case object VeryLarge extends LatexFont {
  val name = "\\Large"
}

case object ExtremelyLarge extends LatexFont {
  val name = "\\LARGE"
}

case object Huge extends LatexFont {
  val name = "\\huge"
}

case object VeryHuge extends LatexFont {
  val name = "\\Huge"
}
