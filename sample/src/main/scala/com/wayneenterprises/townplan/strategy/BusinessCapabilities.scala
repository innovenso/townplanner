package com.wayneenterprises.townplan.strategy

import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.BusinessCapability
import com.innovenso.townplanner.model.concepts.properties.Description

case class BusinessCapabilities()(implicit ea: EnterpriseArchitecture, enterprises: Enterprises) {
  val offeringSuperheroServices: BusinessCapability = ea describes BusinessCapability(title="Offering Superhero Services") as { it =>
    it serves enterprises.wayneCorp
  }
  val managingSecretLairs: BusinessCapability = ea describes BusinessCapability(title="Managing Secret Lairs") as { it =>
    it serves offeringSuperheroServices
    it has Description("Every enterprise that serves as the cover for a superhero needs secret lairs. And when you have secret lairs, you need to manage them.")
  }
  val enterpriseSupport: BusinessCapability = ea describes BusinessCapability(title="Enterprise Support") as { it =>
    it serves enterprises.wayneCorp
  }
  val accounting: BusinessCapability = ea describes BusinessCapability(title = "Accounting") as { it =>
    it serves enterpriseSupport
  }

}
