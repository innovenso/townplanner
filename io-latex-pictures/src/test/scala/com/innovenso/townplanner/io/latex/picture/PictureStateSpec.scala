package com.innovenso.townplanner.io.latex.picture

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplan.io.state.InMemoryStateRepository
import com.innovenso.townplanner.io.latex.test.LatexIO
import com.innovenso.townplanner.model.concepts.Enterprise
import com.innovenso.townplanner.model.concepts.views.{
  BusinessCapabilityMap,
  TechnologyRadar
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class PictureStateSpec extends AnyFlatSpec with GivenWhenThen {
  "a picture" should "not be rendered again unless the underlying view changes" in new LatexIO {
    val statefulWriter: TownPlanPictureWriter =
      TownPlanPictureWriter(
        assetRepository,
        InMemoryStateRepository()
      )

    val enterprise: Enterprise = samples.enterprise
    samples.capabilityHierarchy(
      servedEnterprise = Some(enterprise),
      maxLevel = 2
    )
    val viewUnderTest: BusinessCapabilityMap =
      ea needs BusinessCapabilityMap(enterprise)

    statefulWriter.write(townPlan, OutputContext(Nil))
    println("try again")
    statefulWriter.write(townPlan, OutputContext(Nil))

    samples.capability(Some(enterprise))
    println("try again after change")
    statefulWriter.write(townPlan, OutputContext(Nil))

  }
}
