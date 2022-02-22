package com.innovenso.townplanner.io

import com.innovenso.townplanner.io.model.DiagramSpecification
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.views.{
  CompiledSystemContainerView,
  CompiledSystemIntegrationView,
  SystemContainerView
}
import com.innovenso.townplanner.model.language.{
  CompiledView,
  ModelComponent,
  View
}
import plantuml.integration.txt.SystemIntegrationViewDiagram
import plantuml.system.txt.SystemContainerViewDiagram

object DiagramSpecificationWriter {
  def specifications(
      townPlan: TownPlan,
      view: CompiledView[_ <: View]
  ): List[DiagramSpecification] = view match {
    case systemContainerView: CompiledSystemContainerView =>
      List(
        DiagramSpecification(
          view = systemContainerView,
          plantumlSpecification =
            SystemContainerViewDiagram(systemContainerView).body
        )
      )
    case systemIntegrationView: CompiledSystemIntegrationView =>
      List(
        DiagramSpecification(
          view = systemIntegrationView,
          plantumlSpecification =
            SystemIntegrationViewDiagram(systemIntegrationView).body
        )
      )
    case _ => Nil
  }

}
