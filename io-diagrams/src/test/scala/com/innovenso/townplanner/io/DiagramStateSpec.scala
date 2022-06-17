package com.innovenso.townplanner.io

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplan.io.state.{
  InMemoryStateRepository,
  NoStateRepository
}
import com.innovenso.townplanner.model.concepts.views.SystemContainerView
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class DiagramStateSpec extends AnyFlatSpec with GivenWhenThen {
  "a diagram" should "not be rendered again unless the underlying view changes" in new DiagramIO {
    val statefulWriter: TownPlanDiagramWriter =
      TownPlanDiagramWriter(
        targetDirectory.toPath,
        assetRepository,
        InMemoryStateRepository()
      )

    val system = samples.system()
    val viewUnderTest: SystemContainerView =
      ea needs SystemContainerView(system)

    statefulWriter.write(townPlan, OutputContext(Nil))
    println("try again")
    statefulWriter.write(townPlan, OutputContext(Nil))
  }
}
