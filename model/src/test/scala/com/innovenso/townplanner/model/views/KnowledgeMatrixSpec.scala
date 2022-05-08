package com.innovenso.townplanner.model.views

import com.innovenso.townplanner.model.concepts.properties.BeMigrated
import com.innovenso.townplanner.model.concepts.relationships.{
  Expert,
  HighlyKnowledgeable,
  Knowledgeable,
  Learner,
  NoKnowledge
}
import com.innovenso.townplanner.model.concepts.{
  EnterpriseArchitectureContext,
  Language,
  Tool
}
import com.innovenso.townplanner.model.concepts.views.{
  KnowledgeMatrix,
  TaggedKnowledgeMatrix,
  TechnologyRadar
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class KnowledgeMatrixSpec extends AnyFlatSpec with GivenWhenThen {
  "a knowledge matrix" should "contain all technologies, and have a knowledge level for each of them for every member of a team" in new EnterpriseArchitectureContext {
    Given("some technologies")
    val tool = samples.tool
    val language = samples.language
    val technique = samples.technique
    val platform = samples.platformTechnology
    And("a team")
    val team = samples.team
    val member1 = samples.teamMember(team)
    val member2 = samples.teamMember(team)
    val member3 = samples.teamMember(team)
    And("some knowledge")
    samples.knowledge(member1, tool, Expert)
    samples.knowledge(member2, language, Knowledgeable)
    samples.knowledge(member3, technique, Learner)
    samples.knowledge(member3, platform, HighlyKnowledgeable)
    When("A knowledge matrix is requested")
    val matrixWithMigratedTool =
      ea needs KnowledgeMatrix(forTeam = team)
    val compiledMatrixWithMigratedTool =
      townPlan.knowledgeMatrix(matrixWithMigratedTool.key).get
    Then(
      "the knowledge matrix contains all knowledges"
    )
    assert(compiledMatrixWithMigratedTool.technologies.nonEmpty)
    assert(compiledMatrixWithMigratedTool.team.nonEmpty)
    assert(compiledMatrixWithMigratedTool.members.nonEmpty)
    assert(compiledMatrixWithMigratedTool.level(member1, tool) == Expert)
    assert(
      compiledMatrixWithMigratedTool.level(member1, language) == NoKnowledge
    )
    assert(
      compiledMatrixWithMigratedTool.level(member2, language) == Knowledgeable
    )
    assert(compiledMatrixWithMigratedTool.level(member3, technique) == Learner)
    assert(
      compiledMatrixWithMigratedTool.level(
        member3,
        platform
      ) == HighlyKnowledgeable
    )
    assert(
      compiledMatrixWithMigratedTool.level(member3, language) == NoKnowledge
    )
  }

  "A tagged knowledge matrix" should "contain only technologies with those tags" in new EnterpriseArchitectureContext {
    Given("some technologies")
    val tag1 = samples.tag
    val tool = ea describes Tool(title = "Tool") as { it =>
      it isTagged tag1
    }
    val language = samples.language
    val technique = samples.technique
    val platform = samples.platformTechnology
    And("a team")
    val team = samples.team
    val member1 = samples.teamMember(team)
    val member2 = samples.teamMember(team)
    val member3 = samples.teamMember(team)
    And("some knowledge")
    samples.knowledge(member1, tool, Expert)
    samples.knowledge(member2, language, Knowledgeable)
    samples.knowledge(member3, technique, Learner)
    samples.knowledge(member3, platform, HighlyKnowledgeable)
    When("a tagged knowledge matrix is requested")
    val matrixWithTag =
      ea needs TaggedKnowledgeMatrix(forTeam = team, tags = List(tag1))
    val compiledMatrixWithTag = townPlan.knowledgeMatrix(matrixWithTag.key).get
    Then("the matrix contains only the tagged technologies")
    assert(compiledMatrixWithTag.technologies.size == 1)
  }
}
