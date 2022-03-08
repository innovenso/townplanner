package com.wayneenterprises.townplan.application

import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.ArchitectureBuildingBlock
import com.wayneenterprises.townplan.strategy.{
  BusinessCapabilities,
  Enterprises
}

case class BuildingBlocks()(implicit
    ea: EnterpriseArchitecture,
    capabilities: BusinessCapabilities,
    enterprises: Enterprises
) {
  val lairManagement: ArchitectureBuildingBlock =
    ea describes ArchitectureBuildingBlock(title = "Lair Management") as { it =>
      it realizes capabilities.managingSecretLairs
      it serves enterprises.wayneCorp
    }
}
