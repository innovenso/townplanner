package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.Description
import com.innovenso.townplanner.model.meta.Color
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class BusinessCapabilitySpec extends AnyFlatSpec with GivenWhenThen {
  "Business Capabilities" can "be added to the town plan" in new EnterpriseArchitectureContext {
    val innovenso: Enterprise = ea has Enterprise(title = "Innovenso")

    val green: Tag = ea has Tag(title = "Green", color = Color(0,255,0))
    val red: Tag = ea has Tag(title = "Red", color = Color(255,0,0))

    val marketing: BusinessCapability =
      ea describes BusinessCapability(title = "Marketing") as { it =>
        it has Description(
          "One of the most important capabilities of a modern enterprise"
        )
        it serves innovenso
        it isTagged green
        it isTagged red
      }

    val customerSegmentation: BusinessCapability =
      ea describes BusinessCapability(title = "Customer Segmentation") as {
        it =>
          it serves marketing
      }

    assert(exists(innovenso))
    assert(exists(marketing))
    assert(exists(customerSegmentation))
    assert(townPlan.tags(marketing).contains(green))
    assert(townPlan.tags(marketing).contains(red))
    assert(townPlan.taggedComponents(green, classOf[BusinessCapability]).contains(marketing))
    assert(townPlan.taggedComponents(red, classOf[BusinessCapability]).contains(marketing))
    assert(townPlan.businessCapability(marketing.key).exists(cap => cap.tags.size == 2))
    assert(townPlan.relationships.size == 2)
    assert(townPlan.businessCapabilityMap(innovenso).size == 2)
  }

}
