package com.innovenso.townplanner.io.latex.picture

import com.innovenso.townplanner.io.latex.test.LatexIO
import com.innovenso.townplanner.model.concepts.Enterprise
import com.innovenso.townplanner.model.concepts.relationships.{
  Expert,
  HighlyKnowledgeable,
  Knowledgeable,
  Learner
}
import com.innovenso.townplanner.model.concepts.views.{
  ArchitectureDecisionRecord,
  CompiledArchitectureDecisionRecord,
  KnowledgeMatrix
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec
import tikz.txt.{KnowledgeMatrixDiagram, SecurityImpactDiagram}

class KnowledgeMatrixDiagramSpec extends AnyFlatSpec with GivenWhenThen {
  "A knowledge matrix diagram" should "be rendered for a team" in new LatexIO {
    Given("some technologies")
    val tool = samples.tool
    val language = samples.language
    val technique = samples.technique
    val platform = samples.platformTechnology
    (1 to 4).foreach(_ => samples.language)
    (1 to 2).foreach(_ => samples.tool)
    (1 to 3).foreach(_ => samples.technique)
    (1 to 5).foreach(_ => samples.platformTechnology)
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

    val latexSourceCode: String = KnowledgeMatrixDiagram(
      compiledMatrix,
      townPlan
    ).body

    println(latexSourceCode)

    assert(
      assetsExistWhen(
        pdfIsWritten(latexSourceCode)
      )
    )

  }
}
