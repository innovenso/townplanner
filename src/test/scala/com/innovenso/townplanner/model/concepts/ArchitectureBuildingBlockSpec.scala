package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.meta.Title
import com.innovenso.townplanner.model.test.Factory
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

import scala.util.Try

class ArchitectureBuildingBlockSpec extends AnyFlatSpec with GivenWhenThen {
  "An Architecture Building Block" should "be addable to a townplan" in new Factory {
    val result: Try[(TownPlan, ArchitectureBuildingBlock)] =
      factory.withArchitectureBuildingBlock(
        title = Title("Actor 1")
      )
    assert(result.isSuccess)
    val townPlan: TownPlan = result.get._1
    val buildingBlock: ArchitectureBuildingBlock = result.get._2
    assert(townPlan.has(buildingBlock))
    println(townPlan)
  }

  "An Architecture Building Block" should "be able to belong to an enterprise" in new Factory {
    val result: Try[(TownPlan, ArchitectureBuildingBlock)] = factory
      .withEnterprise(title = Title("Innovenso BV"))
      .flatMap(te =>
        factory.withEnterpriseArchitectureBuildingBlock(
          title = Title("Jurgen Lust"),
          enterprise = te._2
        )
      )
    assert(result.isSuccess)
    val townPlan: TownPlan = result.get._1
    val buildingBlock: ArchitectureBuildingBlock = result.get._2
    assert(townPlan.has(buildingBlock))
    assert(townPlan.has(townPlan.enterprise(buildingBlock).get))
    println(townPlan)
  }
}
