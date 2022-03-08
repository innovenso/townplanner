package com.innovenso.townplanner.io

import com.innovenso.townplan.repository.AssetRepository
import com.innovenso.townplanner.io.model.DiagramSpecification
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.views._
import com.innovenso.townplanner.model.language.{CompiledView, View}
import plantuml.buildingblock.txt.ArchitectureBuildingBlockRealizationDiagram
import plantuml.capability.txt.BusinessCapabilityPositionDiagram
import plantuml.enterprise.txt.{BusinessCapabilityMindMap, FullTownPlanDiagram}
import plantuml.integration.txt.{
  IntegrationMapDiagram,
  SystemIntegrationInteractionDiagram,
  SystemIntegrationInteractionSequenceDiagram,
  SystemIntegrationViewDiagram
}
import plantuml.project.txt.{
  MilestoneArchitectureBuildingBlockImpactView,
  MilestoneBusinessCapabilityImpactView,
  MilestoneItContainerImpactView,
  MilestoneItPlatformImpactView,
  MilestoneItSystemImpactView,
  MilestoneItSystemIntegrationImpactView,
  MilestoneTechnologyImpactView
}
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
    case businessCapabilityPosition: CompiledBusinessCapabilityPosition =>
      List(
        DiagramSpecification(
          businessCapabilityPosition,
          BusinessCapabilityPositionDiagram(businessCapabilityPosition).body
        )
      )
    case architectureBuildingBlockRealizationView: CompiledArchitectureBuildingBlockRealizationView =>
      List(
        DiagramSpecification(
          architectureBuildingBlockRealizationView,
          ArchitectureBuildingBlockRealizationDiagram(
            architectureBuildingBlockRealizationView
          ).body,
          if (architectureBuildingBlockRealizationView.view.includeContainers)
            Some("Detailed")
          else None
        )
      )
    case integrationMap: CompiledIntegrationMap =>
      List(
        DiagramSpecification(
          integrationMap,
          IntegrationMapDiagram(integrationMap).body
        )
      )
    case fullTownPlanView: CompiledFullTownPlanView =>
      List(
        DiagramSpecification(
          fullTownPlanView,
          FullTownPlanDiagram(fullTownPlanView).body
        )
      )
    case systemIntegrationInteractionView: CompiledSystemIntegrationInteractionView =>
      List(
        DiagramSpecification(
          systemIntegrationInteractionView,
          SystemIntegrationInteractionDiagram(
            systemIntegrationInteractionView
          ).body
        ),
        DiagramSpecification(
          systemIntegrationInteractionView,
          SystemIntegrationInteractionSequenceDiagram(
            systemIntegrationInteractionView
          ).body,
          Some("Sequence")
        )
      )
    case projectMilestoneImpactView: CompiledProjectMilestoneImpactView =>
      List(
        DiagramSpecification(
          projectMilestoneImpactView,
          MilestoneItContainerImpactView(projectMilestoneImpactView).body,
          Some("Containers")
        ),
        DiagramSpecification(
          projectMilestoneImpactView,
          MilestoneItSystemImpactView(projectMilestoneImpactView).body,
          Some("Systems")
        ),
        DiagramSpecification(
          projectMilestoneImpactView,
          MilestoneItPlatformImpactView(projectMilestoneImpactView).body,
          Some("Platforms")
        ),
        DiagramSpecification(
          projectMilestoneImpactView,
          MilestoneItSystemIntegrationImpactView(
            projectMilestoneImpactView
          ).body,
          Some("System Integrations")
        ),
        DiagramSpecification(
          projectMilestoneImpactView,
          MilestoneTechnologyImpactView(projectMilestoneImpactView).body,
          Some("Technologies")
        ),
        DiagramSpecification(
          projectMilestoneImpactView,
          MilestoneBusinessCapabilityImpactView(
            projectMilestoneImpactView
          ).body,
          Some("Business Capabilities")
        ),
        DiagramSpecification(
          projectMilestoneImpactView,
          MilestoneArchitectureBuildingBlockImpactView(
            projectMilestoneImpactView
          ).body,
          Some("Architecture Building Blocks")
        )
      )
    case _ => Nil
  }

}
