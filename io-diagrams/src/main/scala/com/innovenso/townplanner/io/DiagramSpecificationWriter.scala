package com.innovenso.townplanner.io

import com.innovenso.townplanner.io.model.DiagramSpecification
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.views.{
  CompiledBusinessCapabilityMap,
  CompiledFlowView,
  CompiledSystemContainerView,
  CompiledSystemIntegrationView,
  SystemContainerView
}
import com.innovenso.townplanner.model.language.{
  CompiledView,
  ModelComponent,
  View
}
import plantuml.enterprise.txt.BusinessCapabilityMindMap
import plantuml.integration.txt.SystemIntegrationViewDiagram
import plantuml.system.txt.SystemContainerViewDiagram
import plantuml.view.txt.{FlowViewDiagram, FlowViewSequenceDiagram}

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
    case flowView: CompiledFlowView =>
      List(
        DiagramSpecification(
          view = flowView,
          plantumlSpecification = FlowViewDiagram(flowView).body
        ),
        DiagramSpecification(
          flowView,
          FlowViewSequenceDiagram(flowView).body,
          Some("Sequence")
        )
      )
    case businessCapabilityMap: CompiledBusinessCapabilityMap =>
      List(
        DiagramSpecification(
          businessCapabilityMap,
          BusinessCapabilityMindMap(businessCapabilityMap).body
        )
      )
    case _ => Nil
  }

}
