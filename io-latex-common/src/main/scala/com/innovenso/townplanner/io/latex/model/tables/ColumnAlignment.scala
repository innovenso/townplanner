package com.innovenso.townplanner.io.latex.model.tables

trait ColumnAlignment {
  def value: String
}

case object ColumnLeft extends ColumnAlignment {
  val value = "l"
}

case object ColumnRight extends ColumnAlignment {
  val value = "r"
}

case object ColumnCenter extends ColumnAlignment {
  val value = "c"
}

case class ColumnParagraph(width: Int) extends ColumnAlignment {
  val value = s"p{${width}mm}"
}
