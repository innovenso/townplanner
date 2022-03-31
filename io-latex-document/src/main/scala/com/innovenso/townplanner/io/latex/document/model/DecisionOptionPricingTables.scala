package com.innovenso.townplanner.io.latex.document.model

import com.innovenso.townplanner.io.latex.model.tables.{ColumnParagraph, ColumnRight, LatexBodyRow, LatexEmptyCell, LatexFooterRow, LatexHeaderRow, LatexSecondaryHeaderRow, LatexTable, LatexTableRow, LatexTextCell}
import com.innovenso.townplanner.model.concepts.properties.{Capex, Cost, Opex}
import com.innovenso.townplanner.model.concepts.views.DecisionOptionDecorator
import com.innovenso.townplanner.model.meta.{MonetaryAmount, Year}

import java.util.Currency

case class DecisionOptionPricingTables(option: DecisionOptionDecorator) {
  val tables: List[LatexTable] = option.option.costFiscalYears.map(year => DecisionOptionPricingYearSection(option, year).table)
}

case class DecisionOptionPricingYearSection(option: DecisionOptionDecorator, fiscalYear: Year) {
  val headerRow: LatexSecondaryHeaderRow = LatexSecondaryHeaderRow(List(
    LatexEmptyCell, LatexEmptyCell, LatexTextCell("Capex"), LatexTextCell("Opex"), LatexTextCell("Remarks")
  ))

  val secondaryHeaderRow: LatexHeaderRow = LatexHeaderRow(List(
    LatexTextCell(s"${fiscalYear.value}", colspan = 5)
  ))

  val bodyRows: List[LatexBodyRow] = option.option.costs(fiscalYear).map(cost => LatexBodyRow(List(
    LatexTextCell(cost.title),
    LatexTextCell(cost.category.value.getOrElse("")),
    LatexTextCell(capex(cost)),
    LatexTextCell(opex(cost)),
    LatexTextCell(cost.description)
  )))

  val currencies: List[Currency] = option.option.costs.map(_.totalCost.currency).distinct

  val footerRows: List[LatexFooterRow] = currencies.map(currency => LatexFooterRow(List(
    LatexTextCell(s"total for ${fiscalYear.value}"), LatexEmptyCell, LatexTextCell(amount(option.option.totalCapex(fiscalYear, currency))), LatexTextCell(amount(option.option.totalOpex(fiscalYear, currency))), LatexEmptyCell
  )))

  def amount(monetaryAmount: MonetaryAmount): String = monetaryAmount.currency.getSymbol + f"${monetaryAmount.amount}%1.2f"

  def capex(c: Cost): String = c match {
    case cap: Capex => amount(cap.totalCost)
    case _ => ""
  }
  def opex(c: Cost): String =  c match {
    case op: Opex => amount(op.totalCost)
    case _ => ""
  }
  val rows: List[LatexTableRow] = List(secondaryHeaderRow, headerRow) ::: bodyRows ::: footerRows

  val table: LatexTable = LatexTable(List(ColumnParagraph(25), ColumnParagraph(20), ColumnRight, ColumnRight, ColumnParagraph(25)), rows)

}