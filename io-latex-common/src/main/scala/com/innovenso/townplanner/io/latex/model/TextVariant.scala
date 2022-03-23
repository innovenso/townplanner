package com.innovenso.townplanner.io.latex.model

trait TextVariant {
  def latexCommandName: String
  def apply(text: String): String = s"\\${latexCommandName}{${text}}"
}

object Bold extends TextVariant {
  val latexCommandName = "textbf"
}

object Uppercase extends TextVariant {
  val latexCommandName = "uppercase"
}
