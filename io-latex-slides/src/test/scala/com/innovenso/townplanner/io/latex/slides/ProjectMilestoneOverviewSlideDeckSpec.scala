package com.innovenso.townplanner.io.latex.slides

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplanner.model.concepts.{
  Decision,
  Enterprise,
  ItProject,
  NotStarted
}
import com.innovenso.townplanner.model.concepts.views.{
  ArchitectureDecisionRecord,
  ProjectMilestoneOverview
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class ProjectMilestoneOverviewSlideDeckSpec
    extends AnyFlatSpec
    with GivenWhenThen {
  "a project milestone overview" should "have its own slide deck" in new LatexSlideDeckIO {
    Given("a project with milestones")
    val innovenso: Enterprise = samples.enterprise
    val project: ItProject = samples.project()
    When("A Project Milestone Overview is requested")
    val overview: ProjectMilestoneOverview = ea needs ProjectMilestoneOverview(
      townPlan.itProjectMilestones(project).head
    )
    When("the slide decks are written")
    val pictureOutputContext: OutputContext = picturesAreWritten(overview.key)
    println(assetRepository.targetBasePath)
    val outputContext: OutputContext =
      slideDecksAreWritten(overview.key, pictureOutputContext)
    Then("slide decks are available in the output context")
    assert(outputContext.outputs.nonEmpty)
    And("the files exist")
    assert(
      assetRepository.objectNames.forall(
        assetRepository
          .read(_)
          .exists(file => {
            println(file.getAbsolutePath)
            file.canRead
          })
      )
    )

  }
}
