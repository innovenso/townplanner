package com.innovenso.townplanner.model.views

import com.innovenso.townplanner.model.concepts.relationships.{
  Expert,
  HighlyKnowledgeable,
  Knowledgeable,
  Learner,
  NoKnowledge
}
import com.innovenso.townplanner.model.concepts.{
  EnterpriseArchitectureContext,
  Tool
}
import com.innovenso.townplanner.model.concepts.views.{
  KnowledgeMatrix,
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
    val matrix = ea needs KnowledgeMatrix(forTeam = team)
    val compiledMatrix = townPlan.knowledgeMatrix(matrix.key).get
    Then(
      "the knowledge matrix contains all knowledges"
    )
    assert(compiledMatrix.technologies.nonEmpty)
    assert(compiledMatrix.team.nonEmpty)
    assert(compiledMatrix.members.nonEmpty)
    assert(compiledMatrix.level(member1, tool) == Expert)
    assert(compiledMatrix.level(member1, language) == NoKnowledge)
    assert(compiledMatrix.level(member2, language) == Knowledgeable)
    assert(compiledMatrix.level(member3, technique) == Learner)
    assert(compiledMatrix.level(member3, platform) == HighlyKnowledgeable)
    assert(compiledMatrix.level(member3, language) == NoKnowledge)

  }
}
