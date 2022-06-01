package com.innovenso.townplanner.io.latex.model

import com.innovenso.townplanner.io.latex.model.tables.LatexTable
import com.innovenso.townplanner.model.concepts.views.DecisionOptionDecorator
import com.innovenso.townplanner.model.meta.Year

case class DecisionOptionPricingTables(
    option: DecisionOptionDecorator,
    titleColumnWidth: Int = 25,
    categoryColumnWidth: Int = 20,
    descriptionColumnWidth: Int = 25,
    showFiscalYear: Boolean = true
) {
  val tables: List[LatexTable] = option.option.costFiscalYears.map(year =>
    DecisionOptionPricingYearSection(
      option,
      year,
      titleColumnWidth,
      categoryColumnWidth,
      descriptionColumnWidth,
      showFiscalYear
    ).table
  )
  val tablesWithYears: List[(LatexTable, Year)] =
    option.option.costFiscalYears.map(year =>
      (
        DecisionOptionPricingYearSection(
          option,
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
