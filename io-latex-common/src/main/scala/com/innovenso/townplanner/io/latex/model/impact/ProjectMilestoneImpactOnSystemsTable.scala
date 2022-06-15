package com.innovenso.townplanner.io.latex.model.impact

import com.innovenso.townplanner.io.latex.model.Bold
import com.innovenso.townplanner.io.latex.model.tables._
import com.innovenso.townplanner.model.concepts.{ItSystem, ItSystemIntegration}
import com.innovenso.townplanner.model.concepts.relationships.{
  Impact,
  Relationship
}
import com.innovenso.townplanner.model.concepts.views.CompiledProjectMilestoneOverview

case class ProjectMilestoneImpactOnSystemsTable(
    impactView: CompiledProjectMilestoneOverview,
    titleColumnWidth: Int = 50,
    implementationColumnWidth: Int = 50
) {
  val headerRow: LatexTableRow = LatexHeaderRow(
    List(
      LatexTextCell("system"),
      LatexTextCell("criticality"),
      LatexTextCell("impact")
    )
  )

  def sectionHeader(title: String): List[LatexTableRow] = List(
    LatexBodyRow(
      List(
        LatexTextCell(title, variants = List(Bold)),
        LatexEmptyCell,
        LatexEmptyCell
      )
    )
  )

  def systemRows(
      systems: List[ItSystem]
  ): List[LatexTableRow] = systems.map(t =>
    LatexBodyRow(
      List(
        LatexTextCell(t.title),
        LatexTextCell(t.criticality.name),
        impactCell(t)
      )
    )
  )

  def impactRelationship(
      system: ItSystem
  ): Option[Relationship] = impactView.decoratedProjectMilestone
    .map(_.milestone)
    .flatMap(m =>
      impactView
        .relationshipsBetween(m, system, classOf[Impact])
        .headOption
    )
  def impactDescription(system: ItSystem): Option[String] =
    impactRelationship(system)
      .flatMap(_.descriptions.headOption)
      .map(_.value)
  def impactCell(system: ItSystem): LatexTableCell =
    impactDescription(system) match {
      case None            => LatexEmptyCell
      case d: Some[String] => LatexTextCell(d.get)
    }

  val addedSystems: List[LatexTableRow] =
    if (impactView.decoratedProjectMilestone.exists(_.hasAddedSystems))
      sectionHeader("added systems") ::: systemRows(
        impactView.decoratedProjectMilestone
          .map(_.addedSystems.toList.sortBy(_.sortKey))
          .getOrElse(Nil)
      )
    else Nil

  val changedSystems: List[LatexTableRow] =
    if (impactView.decoratedProjectMilestone.exists(_.hasChangedSystems))
      sectionHeader("changed systems") ::: systemRows(
        impactView.decoratedProjectMilestone
          .map(_.changedSystems.toList.sortBy(_.sortKey))
          .getOrElse(Nil)
      )
    else Nil

  val removedSystems: List[LatexTableRow] =
    if (impactView.decoratedProjectMilestone.exists(_.hasRemovedSystems))
      sectionHeader("removed systems") ::: systemRows(
        impactView.decoratedProjectMilestone
          .map(_.removedSystems.toList.sortBy(_.sortKey))
          .getOrElse(Nil)
      )
    else Nil

  val rows: List[LatexTableRow] = List(
    headerRow
  ) ::: addedSystems ::: changedSystems ::: removedSystems

  val table: LatexTable = LatexTable(
    List(
      ColumnParagraph(titleColumnWidth),
      ColumnCenter,
      ColumnParagraph(implementationColumnWidth)
    ),
    rows
  )

}
