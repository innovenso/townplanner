package com.innovenso.townplanner.io.latex.slides

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplan.io.state.InMemoryStateRepository
import com.innovenso.townplanner.io.latex.test.LatexIO
import com.innovenso.townplanner.model.concepts.Enterprise
import com.innovenso.townplanner.model.concepts.views.{
  ArchitectureDecisionRecord,
  BusinessCapabilityMap
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class SlideStateSpec extends AnyFlatSpec with GivenWhenThen {
  "a slide deck" should "not be rendered again unless the underlying view changes" in new LatexIO {
    val statefulWriter: TownPlanSlideDeckWriter =
      TownPlanSlideDeckWriter(
        assetRepository,
        InMemoryStateRepository()
      )

    val enterprise: Enterprise = samples.enterprise
    samples.decision(forEnterprise = Some(enterprise))
    val viewUnderTest: ArchitectureDecisionRecord =
      ea needs ArchitectureDecisionRecord()

    statefulWriter.write(townPlan, OutputContext(Nil))
    println("try again")
    statefulWriter.write(townPlan, OutputContext(Nil))
  }
}
