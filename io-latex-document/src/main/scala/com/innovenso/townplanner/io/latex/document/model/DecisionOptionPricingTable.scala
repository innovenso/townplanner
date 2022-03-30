package com.innovenso.townplanner.io.latex.document.model

import com.innovenso.townplanner.io.latex.model.tables.{ColumnParagraph, ColumnRight, LatexBodyRow, LatexEmptyCell, LatexFooterRow, LatexHeaderRow, LatexSecondaryHeaderRow, LatexTable, LatexTableRow, LatexTextCell}
import com.innovenso.townplanner.model.concepts.views.DecisionOptionDecorator
import com.innovenso.townplanner.model.meta.Year

case class DecisionOptionPricingTable (option: DecisionOptionDecorator) {
  val table: LatexTable = LatexTable(List(ColumnParagraph(40), ColumnParagraph(20), ColumnRight, ColumnRight, ColumnParagraph(40)), option.option.costFiscalYears.flatMap(year => DecisionOptionPricingYearSection(option, year).rows))
}

case class DecisionOptionPricingYearSection(option: DecisionOptionDecorator, fiscalYear: Year) {
  val headerRow: LatexHeaderRow = LatexHeaderRow(List(
    LatexEmptyCell, LatexEmptyCell, LatexTextCell("Capex"), LatexTextCell("Opex"), LatexTextCell("Remarks")
  ))

  val secondaryHeaderRow: LatexSecondaryHeaderRow = LatexSecondaryHeaderRow(List(
    LatexTextCell(s"${fiscalYear}", colspan = 2), LatexEmptyCell, LatexEmptyCell, LatexEmptyCell
  ))

  val bodyRows: List[LatexBodyRow] = option.option.costs(fiscalYear).map(cost => LatexBodyRow(List(
    LatexTextCell(cost.title),
    LatexTextCell(cost.category.value.getOrElse("")),
    LatexTextCell("x"),
    LatexTextCell("x"),
    LatexTextCell(cost.description)
  )))

  val footerRow: LatexFooterRow = LatexFooterRow(List(
    LatexTextCell(s"total for ${fiscalYear}", colspan = 2), LatexTextCell("x"), LatexTextCell("x"), LatexEmptyCell
  ))

  val rows: List[LatexTableRow] = List(secondaryHeaderRow, headerRow) ::: bodyRows ::: List(footerRow)
}