package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.{TownPlan, TownPlanFactory}
import com.innovenso.townplanner.model.meta.{Description, Key, SortKey, Title}
import org.scalatest.flatspec.AnyFlatSpec

class EnterpriseSpec extends AnyFlatSpec {
  "A Title" should "be settable on an Enterprise" in {
    val enterprise1: Enterprise =
      Enterprise(key = Key(), sortKey = SortKey(None), title = Title("Enterprise 1"), description = Description(None))
    val enterprise2 = enterprise1.copy(title = Title("Enterprise 2"))
    assert(enterprise1 != enterprise2)
    assert(enterprise1.title.value == "Enterprise 1")
    assert(enterprise2.title.value == "Enterprise 2")
  }

  "An Enterprise" should "be addable to the townplan" in {
    val factory = new TownPlanFactory
    assert(factory.townPlan.modelComponents.isEmpty)
    assert(factory.townPlan.keyPointsInTime.isEmpty)
    val emptyTownPlan = factory.townPlan
    val townPlanWithEnterprise = factory.withEnterprise(title = Title("Innovenso BV"))
    val townPlanWithSecondEnterprise = factory.withEnterprise(key = Key("apple"), sortKey = SortKey(Some("AAP")), title = Title("Apple"), description = Description(Some("creator of the iPhone and other stuff")))
    assert(townPlanWithEnterprise != townPlanWithSecondEnterprise)
    assert(townPlanWithEnterprise != emptyTownPlan)
    assert(townPlanWithSecondEnterprise == factory.townPlan)
  }
}
