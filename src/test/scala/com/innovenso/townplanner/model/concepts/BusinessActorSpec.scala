package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.meta.Title
import com.innovenso.townplanner.model.test.Factory
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

import scala.util.Try

class BusinessActorSpec extends AnyFlatSpec with GivenWhenThen {
  "A Business Actor" should "be addable to a townplan" in new Factory {
    val result: Try[(TownPlan, BusinessActor)] =
      factory.withBusinessActor(
        title = Title("Actor 1"),
        businessActorType = NounActor
      )
    assert(result.isSuccess)
    val townPlan: TownPlan = result.get._1
    val actor: BusinessActor = result.get._2
    assert(townPlan.has(actor))
    println(townPlan)
  }

  "A Business Actor" should "be able to belong to an enterprise" in new Factory {
    val result: Try[(TownPlan, BusinessActor)] = factory
      .withEnterprise(title = Title("Innovenso BV"))
      .flatMap(te =>
        factory.withEnterpriseBusinessActor(
          title = Title("Jurgen Lust"),
          enterprise = te._2,
          businessActorType = IndividualActor
        )
      )
    assert(result.isSuccess)
    val townPlan: TownPlan = result.get._1
    val actor: BusinessActor = result.get._2
    assert(townPlan.has(actor))
    assert(townPlan.has(townPlan.enterprise(actor).get))
    println(townPlan)
  }
}
