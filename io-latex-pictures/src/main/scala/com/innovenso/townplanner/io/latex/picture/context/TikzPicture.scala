package com.innovenso.townplanner.io.latex.picture.context

import com.innovenso.townplan.io.context.OutputType

case object TikzPicture extends OutputType {
  val title: String = "TikZ Picture"
  val description: String =
    "A visualization of a concept or view using PGF/TikZ, for use in LaTeX documents typically."
}

case object TikzRequirementScoreSpiderDiagram extends OutputType {
  val title: String = "Requirement Score Spider Diagram"
  val description: String =
    "Visualizes the scoring on requirements as a spider diagram."
}

case object TikzSecurityImpactDiagram extends OutputType {
  val title: String = "Security Impact Diagram"
  val description: String = "Visualizes the security impact as a CIA matrix"
}

case object TikzBusinessCapabilityOnePager extends OutputType {
  val title: String = "One Pager"
  val description: String =
    "A one-page visualization of the business capability map"
}
