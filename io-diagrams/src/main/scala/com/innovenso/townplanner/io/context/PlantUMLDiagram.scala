package com.innovenso.townplanner.io.context

import com.innovenso.townplan.io.context.OutputType

trait PlantUMLDiagram extends OutputType

case object SimpleDiagram extends PlantUMLDiagram {
  val title: String = "Diagram"
  val description: String =
    "A symbolic representation of information using visualization techniques"
}

case object ProjectMilestoneCurrentStateDiagram extends PlantUMLDiagram {
  val title: String = "Current State Diagram"
  val description: String =
    "The current state in the context of a project milestone"
}

case object ProjectMilestoneTargetStateDiagram extends PlantUMLDiagram {
  val title: String = "Target State Diagram"
  val description: String =
    "The target state in the context of a project milestone"
}

case object ProjectMilestoneArchitectureBuildingBlockImpactDiagram
    extends PlantUMLDiagram {
  val title: String = "Architecture Building Block Impact Diagram"
  val description: String =
    "The impact of a project milestone on architecture building blocks"
}

case object ProjectMilestoneBusinessCapabilityImpactDiagram
    extends PlantUMLDiagram {
  val title: String = "Business Capability Impact Diagram"
  val description: String =
    "The impact of a project milestone on business capabilities"
}

case object ProjectMilestoneItContainerImpactDiagram extends PlantUMLDiagram {
  val title: String = "IT Container Impact Diagram"
  val description: String =
    "The impact of a project milestone on IT containers"
}

case object ProjectMilestoneItPlatformImpactDiagram extends PlantUMLDiagram {
  val title: String = "IT Platform Impact Diagram"
  val description: String =
    "The impact of a project milestone on IT platforms"
}

case object ProjectMilestoneItSystemImpactDiagram extends PlantUMLDiagram {
  val title: String = "IT System Impact Diagram"
  val description: String =
    "The impact of a project milestone on IT systems"
}

case object ProjectMilestoneItSystemIntegrationImpactDiagram
    extends PlantUMLDiagram {
  val title: String = "IT System Integration Impact Diagram"
  val description: String =
    "The impact of a project milestone on system integrations"
}

case object ProjectMilestoneTechnologyImpactDiagram extends PlantUMLDiagram {
  val title: String = "Technology Impact Diagram"
  val description: String =
    "The impact of a project milestone on technologies"
}
