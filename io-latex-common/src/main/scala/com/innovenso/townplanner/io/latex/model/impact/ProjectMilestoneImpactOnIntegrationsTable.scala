package com.innovenso.townplanner.io.latex.model.impact

import com.innovenso.townplanner.io.latex.model.Bold
import com.innovenso.townplanner.io.latex.model.tables.{ColumnParagraph, _}
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.ItSystemIntegration
import com.innovenso.townplanner.model.concepts.properties._
import com.innovenso.townplanner.model.concepts.relationships.{
  CanImpact,
  CanImplement,
  Impact,
  Implementation,
  Relationship
}
import com.innovenso.townplanner.model.concepts.views.{
  CompiledProjectMilestoneImpactView,
  CompiledProjectMilestoneOverview,
  DecisionOptionDecorator,
  ProjectMilestoneImpactView
}

case class ProjectMilestoneImpactOnIntegrationsTable(
    impactView: CompiledProjectMilestoneOverview,
    titleColumnWidth: Int = 50,
    implementationColumnWidth: Int = 40
) {
  val secondaryHeaderRow: LatexTableRow = LatexSecondaryHeaderRow(
    List(
      LatexTextCell("integration"),
      LatexTextCell("attributes", colspan = 3),
      LatexTextCell("impact")
    )
  )

  val headerRow: LatexTableRow = LatexHeaderRow(
    List(
      LatexEmptyCell,
      LatexTextCell("criticality"),
      LatexTextCell("volume"),
      LatexTextCell("frequency"),
      LatexEmptyCell
    )
  )

  def sectionHeader(title: String): List[LatexTableRow] = List(
    LatexBodyRow(
      List(
        LatexTextCell(title, variants = List(Bold)),
        LatexEmptyCell,
        LatexEmptyCell,
        LatexEmptyCell,
        LatexEmptyCell
      )
    )
  )

  def integrationRows(
      integrations: List[ItSystemIntegration]
  ): List[LatexTableRow] = integrations.map(t =>
    LatexBodyRow(
      List(
        LatexTextCell(t.title),
        LatexTextCell(t.criticality.name),
        t.volume
          .map(v => LatexTextCell(v.description))
          .getOrElse(LatexEmptyCell),
        t.frequency
          .map(v => LatexTextCell(v.description))
          .getOrElse(LatexEmptyCell),
        impactCell(t)
      )
    )
  )

  def impactRelationship(
      integration: ItSystemIntegration
  ): Option[Relationship] = impactView.decoratedProjectMilestone
    .map(_.milestone)
    .flatMap(m =>
      impactView
        .relationshipsBetween(m, integration, classOf[Impact])
        .headOption
    )
  def impactDescription(integration: ItSystemIntegration): Option[String] =
    impactRelationship(integration)
      .flatMap(_.descriptions.headOption)
      .map(_.value)
  def impactCell(integration: ItSystemIntegration): LatexTableCell =
    impactDescription(integration) match {
      case None            => LatexEmptyCell
      case d: Some[String] => LatexTextCell(d.get)
    }

  val addedIntegrations: List[LatexTableRow] =
    if (impactView.decoratedProjectMilestone.exists(_.hasAddedIntegrations))
      sectionHeader("added system integrations") ::: integrationRows(
        impactView.decoratedProjectMilestone
          .map(_.addedIntegrations.toList.sortBy(_.sortKey))
          .getOrElse(Nil)
      )
    else Nil

  val changedIntegrations: List[LatexTableRow] =
    if (impactView.decoratedProjectMilestone.exists(_.hasChangedIntegrations))
      sectionHeader("changed system integrations") ::: integrationRows(
        impactView.decoratedProjectMilestone
          .map(_.changedIntegrations.toList.sortBy(_.sortKey))
          .getOrElse(Nil)
      )
    else Nil

  val removedIntegrations: List[LatexTableRow] =
    if (impactView.decoratedProjectMilestone.exists(_.hasRemovedIntegrations))
      sectionHeader("removed system integrations") ::: integrationRows(
        impactView.decoratedProjectMilestone
          .map(_.removedIntegrations.toList.sortBy(_.sortKey))
          .getOrElse(Nil)
      )
    else Nil

  val rows: List[LatexTableRow] = List(secondaryHeaderRow) ::: List(
    headerRow
  ) ::: addedIntegrations ::: changedIntegrations ::: removedIntegrations

  val table: LatexTable = LatexTable(
    List(
      ColumnParagraph(titleColumnWidth),
      ColumnCenter,
      ColumnCenter,
      ColumnCenter,
      ColumnParagraph(implementationColumnWidth)
    ),
    rows
  )

}
