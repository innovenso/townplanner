package com.innovenso.townplanner.io

import com.innovenso.townplan.repository.AssetRepository
import com.innovenso.townplanner.io.model.DiagramSpecification
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.views._
import com.innovenso.townplanner.model.language.{CompiledView, View}
import plantuml.buildingblock.txt.ArchitectureBuildingBlockRealizationDiagram
import plantuml.capability.txt.BusinessCapabilityPositionDiagram
import plantuml.decision.txt.{DecisionArchitectureBuildingBlockImpactView, DecisionBusinessCapabilityImpactView, DecisionItContainerImpactView, DecisionItPlatformImpactView, DecisionItSystemImpactView, DecisionItSystemIntegrationImpactView, DecisionTechnologyImpactView}
import plantuml.enterprise.txt.{BusinessCapabilityMindMap, FullTownPlanDiagram}
import plantuml.integration.txt.{IntegrationMapDiagram, SystemIntegrationInteractionDiagram, SystemIntegrationInteractionSequenceDiagram, SystemIntegrationViewDiagram}
import plantuml.project.txt.{MilestoneArchitectureBuildingBlockImpactView, MilestoneBusinessCapabilityImpactView, MilestoneItContainerImpactView, MilestoneItPlatformImpactView, MilestoneItSystemImpactView, MilestoneItSystemIntegrationImpactView, MilestoneTechnologyImpactView}
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
          relatedModelComponents = systemContainerView.centralSystem.toList,
          plantumlSpecification =
            SystemContainerViewDiagram(systemContainerView).body
        )
      )
    case systemIntegrationView: CompiledSystemIntegrationView =>
      List(
        DiagramSpecification(
          view = systemIntegrationView,
          relatedModelComponents = systemIntegrationView.integration.toList,
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
          view = flowView,
          plantumlSpecification = FlowViewSequenceDiagram(flowView).body,
          filenameAppendix = Some("Sequence")
        )
      )
    case businessCapabilityMap: CompiledBusinessCapabilityMap =>
      List(
        DiagramSpecification(
          view = businessCapabilityMap,
          plantumlSpecification = BusinessCapabilityMindMap(businessCapabilityMap).body,
          relatedModelComponents = businessCapabilityMap.enterprise.toList
        )
      )
    case businessCapabilityPosition: CompiledBusinessCapabilityPosition =>
      List(
        DiagramSpecification(
          view = businessCapabilityPosition,
          plantumlSpecification = BusinessCapabilityPositionDiagram(businessCapabilityPosition).body,
          relatedModelComponents = businessCapabilityPosition.capability.toList
        )
      )
    case architectureBuildingBlockRealizationView: CompiledArchitectureBuildingBlockRealizationView =>
      List(
        DiagramSpecification(
          view = architectureBuildingBlockRealizationView,
          plantumlSpecification = ArchitectureBuildingBlockRealizationDiagram(
            architectureBuildingBlockRealizationView
          ).body,
          filenameAppendix = if (architectureBuildingBlockRealizationView.view.includeContainers)
            Some("Detailed")
          else None,
          relatedModelComponents = architectureBuildingBlockRealizationView.buildingBlock.toList
        )
      )
    case integrationMap: CompiledIntegrationMap =>
      List(
        DiagramSpecification(
          view = integrationMap,
          plantumlSpecification = IntegrationMapDiagram(integrationMap).body,
          relatedModelComponents = townPlan.enterprises
        )
      )
    case fullTownPlanView: CompiledFullTownPlanView =>
      List(
        DiagramSpecification(
          view = fullTownPlanView,
          plantumlSpecification = FullTownPlanDiagram(fullTownPlanView).body,
          relatedModelComponents = fullTownPlanView.enterprise.toList
        )
      )
    case systemIntegrationInteractionView: CompiledSystemIntegrationInteractionView =>
      List(
        DiagramSpecification(
          view = systemIntegrationInteractionView,
          plantumlSpecification = SystemIntegrationInteractionDiagram(
            systemIntegrationInteractionView
          ).body,
          relatedModelComponents = systemIntegrationInteractionView.integration.toList
        ),
        DiagramSpecification(
          view = systemIntegrationInteractionView,
          plantumlSpecification = SystemIntegrationInteractionSequenceDiagram(
            systemIntegrationInteractionView
          ).body,
          filenameAppendix = Some("Sequence"),
          relatedModelComponents = systemIntegrationInteractionView.integration.toList
        )
      )
    case projectMilestoneImpactView: CompiledProjectMilestoneImpactView =>
      List(
        DiagramSpecification(
          view = projectMilestoneImpactView,
          plantumlSpecification = MilestoneItContainerImpactView(projectMilestoneImpactView).body,
          filenameAppendix = Some("Containers"),
          relatedModelComponents = List(projectMilestoneImpactView.milestone)
        ),
        DiagramSpecification(
          view = projectMilestoneImpactView,
          plantumlSpecification = MilestoneItSystemImpactView(projectMilestoneImpactView).body,
          filenameAppendix = Some("Systems"),
          relatedModelComponents = List(projectMilestoneImpactView.milestone)
        ),
        DiagramSpecification(
          view = projectMilestoneImpactView,
          plantumlSpecification = MilestoneItPlatformImpactView(projectMilestoneImpactView).body,
          filenameAppendix = Some("Platforms"),
          relatedModelComponents = List(projectMilestoneImpactView.milestone)
        ),
        DiagramSpecification(
          view = projectMilestoneImpactView,
          plantumlSpecification = MilestoneItSystemIntegrationImpactView(
            projectMilestoneImpactView
          ).body,
          filenameAppendix = Some("System Integrations"),
          relatedModelComponents = List(projectMilestoneImpactView.milestone)
        ),
        DiagramSpecification(
          view = projectMilestoneImpactView,
          plantumlSpecification = MilestoneTechnologyImpactView(projectMilestoneImpactView).body,
          filenameAppendix = Some("Technologies"),
          relatedModelComponents = List(projectMilestoneImpactView.milestone)
        ),
        DiagramSpecification(
          view = projectMilestoneImpactView,
          plantumlSpecification = MilestoneBusinessCapabilityImpactView(
            projectMilestoneImpactView
          ).body,
          filenameAppendix = Some("Business Capabilities"),
          relatedModelComponents = List(projectMilestoneImpactView.milestone)
        ),
        DiagramSpecification(
          view = projectMilestoneImpactView,
          plantumlSpecification = MilestoneArchitectureBuildingBlockImpactView(
            projectMilestoneImpactView
          ).body,
          filenameAppendix = Some("Architecture Building Blocks"),
          relatedModelComponents = List(projectMilestoneImpactView.milestone)
        )
      )
    case decisionImpactView: CompiledDecisionImpactView =>
      List(
        DiagramSpecification(
          view = decisionImpactView,
          plantumlSpecification = DecisionItContainerImpactView(decisionImpactView).body,
          filenameAppendix = Some("Containers"),
          relatedModelComponents = List(decisionImpactView.decision)
        ),
        DiagramSpecification(
          view = decisionImpactView,
          plantumlSpecification = DecisionItSystemImpactView(decisionImpactView).body,
          filenameAppendix = Some("Systems"),
          relatedModelComponents = List(decisionImpactView.decision)
        ),
        DiagramSpecification(
          view = decisionImpactView,
          plantumlSpecification = DecisionItPlatformImpactView(decisionImpactView).body,
          filenameAppendix = Some("Platforms"),
          relatedModelComponents = List(decisionImpactView.decision)
        ),
        DiagramSpecification(
          view = decisionImpactView,
          plantumlSpecification = DecisionItSystemIntegrationImpactView(
            decisionImpactView
          ).body,
          filenameAppendix = Some("System Integrations"),
          relatedModelComponents = List(decisionImpactView.decision)
        ),
        DiagramSpecification(
          view = decisionImpactView,
          plantumlSpecification = DecisionTechnologyImpactView(decisionImpactView).body,
          filenameAppendix = Some("Technologies"),
          relatedModelComponents = List(decisionImpactView.decision)
        ),
        DiagramSpecification(
          view = decisionImpactView,
          plantumlSpecification = DecisionBusinessCapabilityImpactView(
            decisionImpactView
          ).body,
          filenameAppendix = Some("Business Capabilities"),
          relatedModelComponents = List(decisionImpactView.decision)
        ),
        DiagramSpecification(
          view = decisionImpactView,
          plantumlSpecification = DecisionArchitectureBuildingBlockImpactView(
            decisionImpactView
          ).body,
          filenameAppendix = Some("Architecture Building Blocks"),
          relatedModelComponents = List(decisionImpactView.decision)
        )
      )
    case _ => Nil
  }

}
