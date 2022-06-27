package com.innovenso.townplanner.io.latex.document

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplanner.model.concepts.views._
import com.innovenso.townplanner.model.concepts.{Enterprise, ItProject}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class ProjectMilestoneOverviewDocumentSpec
    extends AnyFlatSpec
    with GivenWhenThen {
  "a project milestone overview" should "have its own slide deck" in new LatexDocumentIO {
    Given("a project with milestones")
    val innovenso: Enterprise = samples.enterprise
    val project: ItProject = samples.project()
    When("A Project Milestone Overview is requested")
    val impact: ProjectMilestoneImpactView =
      ea needs ProjectMilestoneImpactView(
        townPlan.itProjectMilestones(project).head
      )
    val transitionStates: ProjectMilestoneTransitionSystemContainerView =
      ea needs ProjectMilestoneTransitionSystemContainerView(
        townPlan.itProjectMilestones(project).head
      )
    townPlan.systemIntegrations.foreach(integration => {
      ea needs SystemIntegrationView(integration)
      ea needs SystemIntegrationInteractionView(integration)
    })
    townPlan.systems.foreach(s => {
      ea needs SystemContainerView(s)
    })
    val overview: ProjectMilestoneOverview = ea needs ProjectMilestoneOverview(
      townPlan.itProjectMilestones(project).head
    )
    When("the documents are written")
    val diagramOutputContext: OutputContext = diagramsAreWritten
    val pictureOutputContext: OutputContext =
      picturesAreWritten(overview.key, diagramOutputContext)
    println(assetRepository.targetBasePath)
    val outputContext: OutputContext =
      documentsAreWritten(overview.key, pictureOutputContext)
    Then("documents are available in the output context")
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
