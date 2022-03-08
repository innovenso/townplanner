package com.wayneenterprises.townplan.strategy

import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.Enterprise
import com.innovenso.townplanner.model.concepts.properties.Description

case class Enterprises()(implicit ea: EnterpriseArchitecture) {
  val wayneCorp: Enterprise =
    ea describes Enterprise(title = "Wayne Enterprises") as { it =>
      it has Description(
        "Wayne Enterprises, Inc., also known as WayneCorp is a fictional company appearing in American comic books published by DC Comics, commonly in association with the superhero Batman. Wayne Enterprises is a large, growing multinational company."
      )
    }
}
