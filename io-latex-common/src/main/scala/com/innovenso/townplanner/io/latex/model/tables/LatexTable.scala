package com.innovenso.townplanner.io.latex.model.tables

case class LatexTable(
    alignment: List[ColumnAlignment] = List(ColumnCenter),
    rows: List[LatexTableRow]
) {
  require(rows.forall(row => row.numberOfColumns == alignment.length))

  val headerRows: List[LatexTableRow] = rows.filter(_.isHeader)
  val footerRows: List[LatexTableRow] = rows.filter(_.isFooter)
  val bodyRows: List[LatexTableRow] = rows.filter(_.isBody)
}
