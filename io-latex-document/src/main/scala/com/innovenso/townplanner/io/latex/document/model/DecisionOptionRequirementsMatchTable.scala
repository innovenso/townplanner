package com.innovenso.townplanner.io.latex.document.model

import com.innovenso.townplanner.io.latex.model.Bold
import com.innovenso.townplanner.io.latex.model.tables._
import com.innovenso.townplanner.model.concepts.properties.{
  AlmostMeetsExpectations,
  DoesNotMeetExpectations,
  ExceedsExpectations,
  MeetsExpectations,
  Requirement,
  RequirementScore
}
import com.innovenso.townplanner.model.concepts.views.DecisionOptionDecorator

case class DecisionOptionRequirementsMatchTable(
    option: DecisionOptionDecorator
) {
  val secondaryHeaderRow: LatexTableRow = LatexSecondaryHeaderRow(
    List(
      LatexTextCell("requirements", colspan = 2),
      LatexTextCell("expectations", colspan = 4)
    )
  )

  val headerRow: LatexTableRow = LatexHeaderRow(
    List(
      LatexEmptyCell,
      LatexTextCell("weight"),
      LatexTextCell("exceeds"),
      LatexTextCell("meets"),
      LatexTextCell("approaches"),
      LatexTextCell("below")
    )
  )

  def sectionHeader(title: String): List[LatexTableRow] = List(
    LatexBodyRow(
      List(
        LatexTextCell(title, variants = List(Bold)),
        LatexEmptyCell,
        LatexEmptyCell,
        LatexEmptyCell,
        LatexEmptyCell,
        LatexEmptyCell
      )
    )
  )

  def requirementRows(
      scores: List[(Requirement, RequirementScore)]
  ): List[LatexTableRow] = scores.map(t =>
    LatexBodyRow(
      List(
        LatexTextCell(t._1.title),
        LatexTextCell(t._1.weight.name),
        scoreCell(t._2, classOf[ExceedsExpectations]),
        scoreCell(t._2, classOf[MeetsExpectations]),
        scoreCell(t._2, classOf[AlmostMeetsExpectations]),
        scoreCell(t._2, classOf[DoesNotMeetExpectations])
      )
    )
  )

  val functionalScores: List[LatexTableRow] =
    if (option.hasFunctionalRequirementScores)
      sectionHeader("functional requirements") ::: requirementRows(
        option.functionalScores
      )
    else Nil

  val qarScores: List[LatexTableRow] =
    if (option.hasQualityAttributeRequirementScores)
      sectionHeader("quality attribute requirements") ::: requirementRows(
        option.qualityAttributeRequirementScores
      )
    else Nil

  val constraintScores: List[LatexTableRow] =
    if (option.hasConstraintScores)
      sectionHeader("constraints") ::: requirementRows(option.constraintScores)
    else Nil

  def scoreCell(
      requirementScore: RequirementScore,
      expectedScore: Class[_ <: RequirementScore]
  ): LatexTableCell = if (expectedScore.isInstance(requirementScore))
    LatexRawTextCell("\\faCheck")
  else LatexEmptyCell

  val rows: List[LatexTableRow] = List(secondaryHeaderRow) ::: List(
    headerRow
  ) ::: functionalScores ::: qarScores ::: constraintScores

  val table: LatexTable = LatexTable(
    List(
      ColumnParagraph(33),
      ColumnParagraph(17),
      ColumnCenter,
      ColumnCenter,
      ColumnCenter,
      ColumnCenter
    ),
    rows
  )

}
