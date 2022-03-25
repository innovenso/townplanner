package com.innovenso.townplanner.io.latex.model.tables

import com.innovenso.townplanner.io.latex.LatexFormat
import com.innovenso.townplanner.io.latex.model.TextVariant

trait LatexTableCell {
  def print: String
  def colspan: Int
}

case object LatexEmptyCell extends LatexTableCell {
  val print = ""
  val colspan = 1
}

case class LatexTextCell(
    value: String,
    variants: List[TextVariant] = Nil,
    colspan: Int = 1
) extends LatexTableCell {
  val cellContents: String = LatexFormat.escapeAndApply(value, variants)
  val colspanPrefix: String =
    if (colspan > 1) s"\\multicolumn{${colspan}}{c}{" else ""
  val colspanSuffix: String = if (colspan > 1) "}" else ""
  val print: String = s"${colspanPrefix}${cellContents}${colspanSuffix}"
}
