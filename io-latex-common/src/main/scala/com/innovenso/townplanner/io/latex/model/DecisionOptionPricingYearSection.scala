package com.innovenso.townplanner.io.latex.model

import com.innovenso.townplanner.io.latex.model.tables._
import com.innovenso.townplanner.model.concepts.properties.{Capex, Cost, Opex}
import com.innovenso.townplanner.model.concepts.views.DecisionOptionDecorator
import com.innovenso.townplanner.model.meta.{MonetaryAmount, Year}

import java.util.Currency

case class DecisionOptionPricingYearSection(
    option: DecisionOptionDecorator,
    fiscalYear: Year,
    titleColumnWidth: Int = 25,
    categoryColumnWidth: Int = 20,
    descriptionColumnWidth: Int = 25,
    showFiscalYear: Boolean = true
) {
  val headerRow: LatexSecondaryHeaderRow = LatexSecondaryHeaderRow(
    List(
      LatexEmptyCell,
      LatexEmptyCell,
      LatexTextCell("Capex"),
      LatexTextCell("Opex"),
      LatexTextCell("Remarks")
    )
  )

  val secondaryHeaderRow: LatexHeaderRow = LatexHeaderRow(
    List(
      LatexTextCell(s"${fiscalYear.value}", colspan = 5)
    )
  )

  val bodyRows: List[LatexBodyRow] = option.option
    .costs(fiscalYear)
    .map(cost =>
      LatexBodyRow(
        List(
          LatexTextCell(cost.title),
          LatexTextCell(cost.category.value.getOrElse("")),
          LatexTextCell(capex(cost)),
          LatexTextCell(opex(cost)),
          LatexTextCell(cost.description)
        )
      )
    )

  val currencies: List[Currency] =
    option.option.costs.map(_.totalCost.currency).distinct

  val footerRows: List[LatexFooterRow] = currencies.map(currency =>
    LatexFooterRow(
      List(
        LatexTextCell(s"total for ${fiscalYear.value}"),
        LatexEmptyCell,
        LatexTextCell(amount(option.option.totalCapex(fiscalYear, currency))),
        LatexTextCell(amount(option.option.totalOpex(fiscalYear, currency))),
        LatexEmptyCell
      )
    )
  )

  def amount(monetaryAmount: MonetaryAmount): String =
    monetaryAmount.currency.getSymbol + f"${monetaryAmount.amount}%1.2f"

  def capex(c: Cost): String = c match {
    case cap: Capex => amount(cap.totalCost)
    case _          => ""
  }
  def opex(c: Cost): String = c match {
    case op: Opex => amount(op.totalCost)
    case _        => ""
  }

  val headerRows: List[LatexTableRow] =
    if (showFiscalYear) List(secondaryHeaderRow, headerRow) else List(headerRow)

  val rows: List[LatexTableRow] =
    headerRows ::: bodyRows ::: footerRows

  val table: LatexTable = LatexTable(
    List(
      ColumnParagraph(titleColumnWidth),
      ColumnParagraph(categoryColumnWidth),
      ColumnRight,
      ColumnRight,
      ColumnParagraph(descriptionColumnWidth)
    ),
    rows
  )

}
