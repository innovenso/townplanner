package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.properties.{
  Documentation,
  HasDocumentation
}
import com.innovenso.townplanner.model.meta.{Description, Key, SortKey, Title}
import com.innovenso.townplanner.model.test.Factory
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

import scala.util.Try

class EnterpriseSpec extends AnyFlatSpec with GivenWhenThen {
  "An Enterprise" should "be addable to the townplan" in new Factory {
    assert(factory.townPlan.modelComponents.isEmpty)
    assert(factory.townPlan.keyPointsInTime.isEmpty)
    val emptyTownPlan: TownPlan = factory.townPlan
    val townPlanWithEnterprise: Try[(TownPlan, Enterprise)] =
      factory.withEnterprise(title = Title("Innovenso BV"))
    assert(townPlanWithEnterprise.isSuccess)
    val townPlanWithSecondEnterprise: Try[(TownPlan, Enterprise)] =
      factory.withEnterprise(
        key = Key("apple"),
        sortKey = SortKey(Some("AAP")),
        title = Title("Apple"),
        description = Description(Some("creator of the iPhone and other stuff"))
      )
    val failureBecauseDuplicate: Try[(TownPlan, Enterprise)] =
      factory.withEnterprise(key = Key("apple"), title = Title("Pear"))
    assert(failureBecauseDuplicate.isFailure)
    assert(townPlanWithSecondEnterprise.isSuccess)
    assert(townPlanWithEnterprise.get._1 != townPlanWithSecondEnterprise.get._1)
    assert(townPlanWithEnterprise.get._1 != emptyTownPlan)
    assert(townPlanWithSecondEnterprise.get._1 == factory.townPlan)
  }

  "An Enterprise" should "be able to have documentation" in new Factory {
    val townPlanWithEnterprise: Try[(TownPlan, Enterprise)] =
      factory.withEnterprise(
        key = Key("innovenso"),
        title = Title("Innovenso BV")
      )
    assert(townPlanWithEnterprise.isSuccess)
    val townPlanWithDocumentedEnterprise: Try[(TownPlan, HasDocumentation)] =
      factory.withDocumentation(
        Key("innovenso"),
        Documentation(
          key = Key(),
          sortKey = SortKey(None),
          description = Description(Some("Hello Innovenso"))
        )
      )
    assert(townPlanWithDocumentedEnterprise.isSuccess)
    assert(
      townPlanWithDocumentedEnterprise.get._1
        .enterprise(Key("innovenso"))
        .get
        .documentations
        .head
        .description
        .value
        .get
        .equals("Hello Innovenso")
    )
  }

  "An Enterprise" should "be able to have relationships" in new Factory {
    val townPlanWithEnterprise: Try[(TownPlan, Enterprise)] =
      factory.withEnterprise(
        key = Key("innovenso"),
        title = Title("Innovenso BV")
      )
    assert(townPlanWithEnterprise.isSuccess)
    val townPlanWithEnterprise2: Try[(TownPlan, Enterprise)] =
      factory.withEnterprise(
        key = Key("geniusfish"),
        title = Title("Genius Fish BV")
      )
    assert(townPlanWithEnterprise2.isSuccess)
    val townPlanWithRelationship: Try[(TownPlan, Relationship)] =
      factory.withRelationship(
        key = Key("innogf"),
        title = Title("is composed of"),
        sourceKey = Key("innovenso"),
        targetKey = Key("geniusfish"),
        relationshipType = IsComposedOf
      )
    assert(townPlanWithRelationship.isSuccess)
    assert(
      townPlanWithRelationship.get._1.relationship(Key("innogf")).isDefined
    )
    assert(
      townPlanWithRelationship.get._1
        .relationship(Key("innogf"))
        .get
        .title == Title("is composed of")
    )
    assert(
      townPlanWithRelationship.get._1
        .relationship(Key("innogf"))
        .get
        .source == Key("innovenso")
    )
    assert(
      townPlanWithRelationship.get._1
        .relationship(Key("innogf"))
        .get
        .target == Key("geniusfish")
    )
    val innovenso: Enterprise =
      townPlanWithRelationship.get._1.enterprise(Key("innovenso")).get
    val geniusfish: Enterprise =
      townPlanWithRelationship.get._1.enterprise(Key("geniusfish")).get
    assert(
      townPlanWithRelationship.get._1
        .relationships(innovenso)
        .exists(r => r.key == Key("innogf"))
    )
    assert(townPlanWithRelationship.get._1.relationships(innovenso).size == 1)
    assert(townPlanWithRelationship.get._1.relationships(geniusfish).size == 1)
    println(townPlanWithRelationship)
  }

}
