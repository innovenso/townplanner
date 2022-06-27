package com.innovenso.townplanner.io.latex.model

import com.innovenso.townplanner.io.latex.model.tables.LatexTable
import com.innovenso.townplanner.model.concepts.views.{
  DecisionOptionDecorator,
  ProjectMilestoneDecorator
}
import com.innovenso.townplanner.model.meta.Year

case class ProjectMilestonePricingTables(
    milestoneDecorator: ProjectMilestoneDecorator,
    titleColumnWidth: Int = 25,
    categoryColumnWidth: Int = 20,
    descriptionColumnWidth: Int = 25,
    showFiscalYear: Boolean = true
) {
  val tables: List[LatexTable] =
    milestoneDecorator.milestone.costFiscalYears.map(year =>
      ProjectMilestonePricingYearSection(
        milestoneDecorator,
        year,
        titleColumnWidth,
        categoryColumnWidth,
        descriptionColumnWidth,
        showFiscalYear
      ).table
    )
  val tablesWithYears: List[(LatexTable, Year)] =
    milestoneDecorator.milestone.costFiscalYears.map(year =>
      (
        ProjectMilestonePricingYearSection(
          milestoneDecorator,
          year,
          titleColumnWidth,
          categoryColumnWidth,
          descriptionColumnWidth,
          showFiscalYear
        ).table,
        year
      )
    )
}
