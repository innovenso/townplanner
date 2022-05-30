package com.innovenso.townplanner.model.views

import com.innovenso.townplanner.model.concepts.properties.Description
import com.innovenso.townplanner.model.concepts.views.{
  CompiledProjectMilestoneTransitionSystemContainerView,
  ProjectMilestoneTransitionSystemContainerView
}
import com.innovenso.townplanner.model.concepts.{
  BusinessActor,
  Database,
  Enterprise,
  EnterpriseArchitectureContext,
  ItProject,
  ItProjectMilestone,
  ItSystem,
  Microservice,
  Technology,
  WebUI
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class ProjectMilestoneTransitionSystemContainerViewSpec
    extends AnyFlatSpec
    with GivenWhenThen {
  "a project milestone transition system container view" can "be rendered" in new EnterpriseArchitectureContext {
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
      townPlan.afterProjectMilestoneSystemContainerView(viewUnderTest.key).get

    Then("the after view contains the correct elements")
    println(compiledAfterView.containers)
    println(compiledAfterView.systemContexts)
    assert(compiledAfterView.systemContexts.contains(system1))
    assert(!compiledAfterView.otherSystems.contains(system1))
    assert(compiledAfterView.containers.contains(ms2))
    assert(compiledAfterView.containers.contains(db2))
    assert(compiledAfterView.systemContexts.contains(system2))
    assert(!compiledAfterView.otherSystems.contains(system2))
    assert(!compiledAfterView.systemContexts.contains(system3))
    assert(!compiledAfterView.otherSystems.contains(system3))
    assert(compiledAfterView.containers.contains(ui1))
    assert(compiledAfterView.containers.contains(ms1))
    assert(!compiledAfterView.containers.contains(db1))

    And("the before view contains the correct elements")
    assert(compiledBeforeView.systemContexts.contains(system1))
    assert(!compiledBeforeView.otherSystems.contains(system1))
    assert(!compiledBeforeView.systemContexts.contains(system2))
    assert(!compiledBeforeView.systemContexts.contains(system3))
    assert(compiledBeforeView.otherSystems.contains(system3))
    assert(compiledBeforeView.containers.contains(ui1))
    assert(compiledBeforeView.businessActors.contains(actor1))
    assert(compiledBeforeView.businessActors.contains(actor2))
    assert(!compiledBeforeView.containers.contains(db2))
    assert(!compiledBeforeView.containers.contains(ms2))
    assert(compiledBeforeView.containers.contains(db1))
  }
}
