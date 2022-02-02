package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.properties.{
  ArchitectureVerdict,
  Invest
}
import com.innovenso.townplanner.model.meta.{Description, Title}
import com.innovenso.townplanner.model.test.Factory
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

import scala.util.{Success, Try}

class ArchitectureBuildingBlockSpec extends AnyFlatSpec with GivenWhenThen {
  "An Architecture Building Block" should "be addable to a townplan" in new Factory {
    val result: Try[(TownPlan, ArchitectureBuildingBlock)] =
      factory.withArchitectureBuildingBlock(
        title = Title("Building Block 1")
      )
    assert(result.isSuccess)
    val townPlan: TownPlan = result.get._1
    val buildingBlock: ArchitectureBuildingBlock = result.get._2
    assert(townPlan.has(buildingBlock))
    println(townPlan)
  }

  "An Architecture Building Block" should "have an architecture verdict" in new Factory {
    val result: Try[(TownPlan, ArchitectureBuildingBlock)] =
      factory
        .withArchitectureBuildingBlock(
          title = Title("Building Block 1")
        )
        .flatMap(ta =>
          factory
            .withArchitectureVerdict(
              ta._2,
              ArchitectureVerdict(
                architectureVerdictType = Invest,
                description = Description(Some("we should invest in this"))
              )
            )
        )
    assert(result.isSuccess)
    val townPlan: TownPlan = result.get._1
    val buildingBlock: ArchitectureBuildingBlock = result.get._2
    assert(townPlan.has(buildingBlock))
    println(townPlan)
    assert(buildingBlock.architectureVerdict.architectureVerdictType == Invest)
  }

  "An Architecture Building Block" should "be able to belong to an enterprise" in new Factory {
    val result: Try[(TownPlan, ArchitectureBuildingBlock)] = factory
      .withEnterprise(title = Title("Innovenso BV"))
      .flatMap(te =>
        factory.withEnterpriseArchitectureBuildingBlock(
          title = Title("Building Block 1"),
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
