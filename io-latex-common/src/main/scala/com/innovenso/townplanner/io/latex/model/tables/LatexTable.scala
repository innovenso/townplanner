package com.innovenso.townplanner.io.latex.model.tables

case class LatexTable(
    alignment: List[ColumnAlignment] = List(ColumnCenter),
    rows: List[LatexTableRow]
) {
  require(rows.forall(row => row.numberOfColumns == alignment.length))
  val tableStart: String = s"\\begin{tabular}{${alignment.mkString}}\\toprule"

  val print = ""
}
