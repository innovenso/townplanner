package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  Description,
  Website
}
import com.innovenso.townplanner.model.concepts.relationships.Composition
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class EnterpriseSpec extends AnyFlatSpec with GivenWhenThen {
  "Enterprises" can "be added to the townplan" in new EnterpriseArchitecture {
    val innovenso: Enterprise =
      ea describes Enterprise(title = "Innovenso") as { it =>
        it has Description("hello")
        it has Website(url = "https://innovenso.com")
      }

    val geniusfish: Enterprise =
      ea has Enterprise(title = "genius fish")

    val innovensogroup: Enterprise =
      ea describes Enterprise(title = "Innovenso Group") as { it =>
        it isComposedOf geniusfish
      }

    ea hasRelationship Composition(
      source = innovenso.key,
      target = geniusfish.key,
      title = "hello world"
    )

    assert(townPlan.enterprise(innovenso.key).exists(_.descriptions.nonEmpty))
    assert(townPlan.enterprise(innovenso.key).exists(_.websiteLinks.nonEmpty))
    assert(exists(geniusfish))
    assert(exists(innovensogroup))
    assert(townPlan.relationships.size == 2)
  }
}
