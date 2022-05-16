package com.innovenso.townplanner.io.model

import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.relationships.Relationship

case class FlowTitleDecorator(relationship: Relationship)(implicit
    townPlan: TownPlan
) {
  val title: String = relationship.title + townPlan
    .technologyLabel(relationship)
    .map(l => s" [${l}]")
    .getOrElse("")
}
