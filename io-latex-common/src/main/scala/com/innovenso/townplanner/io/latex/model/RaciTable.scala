package com.innovenso.townplanner.io.latex.model

import com.innovenso.townplanner.io.latex.model.tables._
import com.innovenso.townplanner.model.concepts.Person
import com.innovenso.townplanner.model.concepts.views.DecisionDecorator

case class RaciTable(
    decoratedDecision: DecisionDecorator,
    columnWidth: Int = 36
) {
  private val headerRow: LatexHeaderRow = LatexHeaderRow(
    List("Responsible", "Accountable", "Consulted", "Informed").map(
      LatexTextCell(_, List(Bold))
    )
  )
  private val numberOfRows: Int = List(
    decoratedDecision.responsible.length,
    decoratedDecision.accountable.length,
    decoratedDecision.consulted.length,
    decoratedDecision.informed.length
  ).max
  private val rows = (0 to numberOfRows).map(index => row(index)).toList
  private def row(index: Int): LatexBodyRow = LatexBodyRow(
    List(
      cell(decoratedDecision.responsible, index),
      cell(decoratedDecision.accountable, index),
      cell(decoratedDecision.consulted, index),
      cell(decoratedDecision.informed, index)
    )
  )
  private def cell(persons: List[Person], index: Int): LatexTableCell = persons
    .lift(index)
    .map(person => LatexTextCell(person.title))
    .getOrElse(LatexEmptyCell)
  val table: LatexTable =
    LatexTable(
      (1 to 4).map(_ => ColumnParagraph(columnWidth)).toList,
      headerRow :: rows
    )
}
