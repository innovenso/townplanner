package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.Documentation
import com.innovenso.townplanner.model.meta.{Description, Key, SortKey, Title}
import com.innovenso.townplanner.model.{TownPlan, TownPlanFactory}
import org.scalatest.flatspec.AnyFlatSpec

import java.time.LocalDate
import scala.util.Try

class TownPlanSpec extends AnyFlatSpec {
  trait Factory {
    val factory: TownPlanFactory = new TownPlanFactory
  }

  "An Enterprise" should "be addable to the townplan" in new Factory {
    assert(factory.townPlan.modelComponents.isEmpty)
    assert(factory.townPlan.keyPointsInTime.isEmpty)
    val emptyTownPlan: TownPlan = factory.townPlan
    val townPlanWithEnterprise: Try[TownPlan] =
      factory.withEnterprise(title = Title("Innovenso BV"))
    assert(townPlanWithEnterprise.isSuccess)
    val townPlanWithSecondEnterprise: Try[TownPlan] = factory.withEnterprise(
      key = Key("apple"),
      sortKey = SortKey(Some("AAP")),
      title = Title("Apple"),
      description = Description(Some("creator of the iPhone and other stuff"))
    )
    val failureBecauseDuplicate: Try[TownPlan] =
      factory.withEnterprise(key = Key("apple"), title = Title("Pear"))
    assert(failureBecauseDuplicate.isFailure)
    assert(townPlanWithSecondEnterprise.isSuccess)
    assert(townPlanWithEnterprise.get != townPlanWithSecondEnterprise.get)
    assert(townPlanWithEnterprise.get != emptyTownPlan)
    assert(townPlanWithSecondEnterprise.get == factory.townPlan)
  }

  "An Enterprise" should "be able to have documentation" in new Factory {
    val townPlanWithEnterprise: Try[TownPlan] =
      factory.withEnterprise(
        key = Key("innovenso"),
        title = Title("Innovenso BV")
      )
    assert(townPlanWithEnterprise.isSuccess)
    val townPlanWithDocumentedEnterprise: Try[TownPlan] =
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
      townPlanWithDocumentedEnterprise.get
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
    val townPlanWithEnterprise: Try[TownPlan] =
      factory.withEnterprise(
        key = Key("innovenso"),
        title = Title("Innovenso BV")
      )
    assert(townPlanWithEnterprise.isSuccess)
    val townPlanWithEnterprise2: Try[TownPlan] =
      factory.withEnterprise(
        key = Key("geniusfish"),
        title = Title("Genius Fish BV")
      )
    assert(townPlanWithEnterprise2.isSuccess)
    val townPlanWithRelationship: Try[TownPlan] =
      factory.withRelationship(
        key = Key("innogf"),
        title = Title("is composed of"),
        sourceKey = Key("innovenso"),
        targetKey = Key("geniusfish"),
        relationshipType = IsComposedOf
      )
    assert(townPlanWithRelationship.isSuccess)
    assert(townPlanWithRelationship.get.relationship(Key("innogf")).isDefined)
    assert(
      townPlanWithRelationship.get
        .relationship(Key("innogf"))
        .get
        .title == Title("is composed of")
    )
    assert(
      townPlanWithRelationship.get
        .relationship(Key("innogf"))
        .get
        .source == Key("innovenso")
    )
    assert(
      townPlanWithRelationship.get
        .relationship(Key("innogf"))
        .get
        .target == Key("geniusfish")
    )
    val innovenso: Enterprise =
      townPlanWithRelationship.get.enterprise(Key("innovenso")).get
    val geniusfish: Enterprise =
      townPlanWithRelationship.get.enterprise(Key("geniusfish")).get
    assert(
      townPlanWithRelationship.get
        .relationships(innovenso)
        .exists(r => r.key == Key("innogf"))
    )
    assert(townPlanWithRelationship.get.relationships(innovenso).size == 1)
    assert(townPlanWithRelationship.get.relationships(geniusfish).size == 1)
    println(townPlanWithRelationship)
  }

  "A key point in time" should "be addable to the townplan" in new Factory {
    val townPlanWithKeyPointInTime: Try[TownPlan] =
      factory.withKeyPointInTime(date = LocalDate.now(), title = Title("today"))
    val failureBecauseDuplicate: Try[TownPlan] = factory.withKeyPointInTime(
      date = LocalDate.now(),
      title = Title("also today")
    )
    assert(townPlanWithKeyPointInTime.isSuccess)
    assert(
      townPlanWithKeyPointInTime.get.keyPointsInTime.contains(LocalDate.now())
    )
    assert(failureBecauseDuplicate.isFailure)
  }
}
