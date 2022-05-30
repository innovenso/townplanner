package com.innovenso.townplanner.io

import com.innovenso.townplanner.model.concepts.properties.Description
import com.innovenso.townplanner.model.concepts.views.{
  CompiledProjectMilestoneImpactView,
  CompiledProjectMilestoneTransitionSystemContainerView,
  ProjectMilestoneImpactView,
  ProjectMilestoneTransitionSystemContainerView
}
import com.innovenso.townplanner.model.concepts.{
  BusinessActor,
  Database,
  Enterprise,
  ItProject,
  ItProjectMilestone,
  ItSystem,
  Microservice,
  Technology,
  WebUI
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class ProjectMilestoneTransitionSystemContainerViewDiagramSpec
    extends AnyFlatSpec
    with GivenWhenThen {
  "a project milestone transition system container view" can "be rendered" in new DiagramIO {
    Given("an enterprise")
    val innovenso: Enterprise = samples.enterprise
    val apple: Enterprise = samples.enterprise
    val system1: ItSystem =
      samples.system(withContainers = false)
    val system2: ItSystem = samples.system(withContainers = false)
    val system3: ItSystem = samples.system(withContainers = false)

    And("some containers")
    val ms1: Microservice = samples.microservice(system1)
    val ui1: WebUI = samples.ui(system1)
    val db1: Database = samples.database(system1)

    val ms2: Microservice = samples.microservice(system2)
    val db2: Database = samples.database(system2)

    And("an actor")
    val actor1: BusinessActor = samples.actor
    val actor2: BusinessActor = samples.actor

    samples.flow(ms1, system2)
    samples.flow(actor1, ui1)
    samples.flow(actor2, ui1)
    samples.flow(ui1, ms1)
    samples.flow(ms1, db1)
    samples.flow(ms1, ms2)
    samples.flow(ms2, db2)
    samples.flow(ms1, system3)

    And("some technologies")
    val tech1: Technology = samples.language
    val tech2: Technology = samples.framework

    And("a project milestone impacting them")
    val project: ItProject = ea describes ItProject(title = "the project") as {
      it =>
        it has Description("This project changes things")
    }

    val milestone: ItProjectMilestone =
      ea describes ItProjectMilestone(title = "milestone 1") as { it =>
        it isPartOf project
        it changes system1
        it creates system2
        it removes system3
        it keeps ui1
        it changes ms1
        it creates db2
        it creates ms2
        it removes db1
        it has Description("And this milestone changes some of them")
      }

    And("a project milestone transition system container view")
    val viewUnderTest: ProjectMilestoneTransitionSystemContainerView =
      ea needs ProjectMilestoneTransitionSystemContainerView(
        forProjectMilestone = milestone.key
      )

    val compiledBeforeView
        : CompiledProjectMilestoneTransitionSystemContainerView =
      townPlan.beforeProjectMilestoneSystemContainerView(viewUnderTest.key).get

    val compiledAfterView
        : CompiledProjectMilestoneTransitionSystemContainerView =
      townPlan.beforeProjectMilestoneSystemContainerView(viewUnderTest.key).get

    Then("the specification exists")
    assert(
      specificationExists(
        townPlan.beforeProjectMilestoneSystemContainerView(viewUnderTest.key)
      )
    )
    assert(
      specificationExists(
        townPlan.afterProjectMilestoneSystemContainerView(viewUnderTest.key)
      )
    )

    And("the diagrams are written")
    assert(diagramsAreWritten(viewUnderTest.key))

  }
}
