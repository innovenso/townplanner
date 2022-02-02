package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.meta.Title
import com.innovenso.townplanner.model.test.Factory
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

import scala.util.Try

class BusinessCapabilitySpec extends AnyFlatSpec with GivenWhenThen {
  "A Business Capability" should "be addable to a townplan" in new Factory {
    val result: Try[(TownPlan, BusinessCapability)] =
      factory.withBusinessCapability(title = Title("Capability 1"))
    assert(result.isSuccess)
    val townPlan: TownPlan = result.get._1
    val capability: BusinessCapability = result.get._2
    assert(townPlan.has(capability))
    println(townPlan)
  }

  "A Business Capability" should "be able to have children" in new Factory {
    val result: Try[(TownPlan, BusinessCapability)] = factory
      .withBusinessCapability(title = Title("Level 0"))
      .flatMap(fb =>
        factory
          .withChildBusinessCapability(title = Title("Level 1"), parent = fb._2)
      )
    assert(result.isSuccess)
    val townPlan: TownPlan = result.get._1
    val capability: BusinessCapability = result.get._2
    assert(townPlan.has(capability))
    assert(townPlan.has(townPlan.parentBusinessCapability(capability).get))
    println(townPlan)
  }

  "A Business Capability" should "be able to belong to an enterprise" in new Factory {
    val result: Try[(TownPlan, BusinessCapability)] = factory
      .withEnterprise(title = Title("Innovenso BV"))
      .flatMap(te =>
        factory.withEnterpriseBusinessCapability(
          title = Title("Level 0"),
          enterprise = te._2
        )
      )
    assert(result.isSuccess)
    val townPlan: TownPlan = result.get._1
    val capability: BusinessCapability = result.get._2
    assert(townPlan.has(capability))
    assert(townPlan.has(townPlan.enterprise(capability).get))
    println(townPlan)
  }
}
