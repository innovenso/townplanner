package com.innovenso.townplanner.io

import com.innovenso.townplanner.model.concepts._
import com.innovenso.townplanner.model.concepts.properties.Description
import com.innovenso.townplanner.model.concepts.views.{CompiledDecisionImpactView, CompiledProjectMilestoneImpactView, DecisionImpactView, ProjectMilestoneImpactView}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class DecisionImpactDiagramSpec extends AnyFlatSpec with GivenWhenThen {
  "a decision impact view" can "be rendered" in new DiagramIO {
    Given("a decision")
    val innovenso: Enterprise = samples.enterprise
    val decision: Decision = samples.decision(forEnterprise = Some(innovenso), status = InProgress)

    When("a decision impact view is requested")
    val viewUnderTest: DecisionImpactView =
      ea needs DecisionImpactView(forDecision = decision)

    val compiledView: CompiledDecisionImpactView =
      townPlan.decisionImpactView(viewUnderTest.key).get

    Then("the specification exists")
    assert(
      specificationExists(
        townPlan.decisionImpactView(viewUnderTest.key)
      )
    )

    And("the diagrams are written")
    assert(diagramsAreWritten(viewUnderTest.key))
  }
}
