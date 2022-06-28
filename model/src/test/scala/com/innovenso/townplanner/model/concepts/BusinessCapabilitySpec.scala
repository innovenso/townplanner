package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.Description
import com.innovenso.townplanner.model.meta.Color
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class BusinessCapabilitySpec extends AnyFlatSpec with GivenWhenThen {
  "Business Capabilities" can "be added to the town plan" in new EnterpriseArchitectureContext {
    val innovenso: Enterprise = ea has Enterprise(title = "Innovenso")

    val green: Tag =
      ea has Tag(title = "Green", color = Color(0, 255, 0)("green"))
    val red: Tag = ea has Tag(title = "Red", color = Color(255, 0, 0)("Red"))

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
    assert(
      townPlan
        .taggedComponents(green, classOf[BusinessCapability])
        .contains(marketing)
    )
    assert(
      townPlan
        .taggedComponents(red, classOf[BusinessCapability])
        .contains(marketing)
    )
    assert(
      townPlan
        .businessCapability(marketing.key)
        .exists(cap => cap.tags.size == 2)
    )
    assert(townPlan.relationships.size == 2)
    assert(townPlan.businessCapabilityMap(innovenso).size == 2)
  }

  "business capability map" can "be requested" in new EnterpriseArchitectureContext {
    val enterprise: Enterprise = samples.enterprise
    val capabalities: List[BusinessCapability] =
      samples.capabilityHierarchy(Some(enterprise), None, 2, 0)

    println(s"sample capabilities: ${capabalities.size}")
    val capabilityMap: List[BusinessCapability] =
      townPlan.businessCapabilityMap(enterprise)
    println(s"capability map size: ${capabilityMap.size}")
  }

  "business capability hierarchy" can "be requested for one capability" in new EnterpriseArchitectureContext {
    val enterprise: Enterprise = samples.enterprise
    val cap1 = samples.capability(Some(enterprise))
    val cap2 = samples.capability(Some(enterprise))
    val cap11 = samples.capability(None, Some(cap1))
    val cap12 = samples.capability(None, Some(cap1))
    val cap121 = samples.capability(None, Some(cap12))
    assert(hierarchyIs(cap1, List(cap1, cap11, cap12, cap121)))
    assert(hierarchyIs(cap2, List(cap2)))
    assert(hierarchyIs(cap11, List(cap1, cap11)))
    assert(hierarchyIs(cap12, List(cap1, cap12, cap121)))
    assert(hierarchyIs(cap121, List(cap1, cap12, cap121)))

    def hierarchyIs(
        of: BusinessCapability,
        shouldBe: List[BusinessCapability]
    ): Boolean = {
      val h = townPlan.businessCapabilityHierarchy(of)
      h.size == shouldBe.size && shouldBe.forall(h.contains)
    }
  }
}
