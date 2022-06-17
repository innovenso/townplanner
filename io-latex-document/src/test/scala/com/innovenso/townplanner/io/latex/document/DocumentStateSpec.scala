package com.innovenso.townplanner.io.latex.document

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplan.io.state.InMemoryStateRepository
import com.innovenso.townplanner.model.concepts.Enterprise
import com.innovenso.townplanner.model.concepts.views.{
  BusinessCapabilityMap,
  FullTownPlanView,
  SystemContainerView,
  TechnologyRadar
}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class DocumentStateSpec extends AnyFlatSpec with GivenWhenThen {
  "a document" should "not be rendered again unless the underlying view changes" in new LatexDocumentIO {
    val statefulWriter: TownPlanDocumentWriter =
      TownPlanDocumentWriter(
        assetRepository,
        InMemoryStateRepository()
      )

    val enterprise: Enterprise = samples.enterprise
    samples.capabilityHierarchy(
      servedEnterprise = Some(enterprise),
      maxLevel = 2
    )
    val viewUnderTest: TechnologyRadar =
      ea needs TechnologyRadar()

    statefulWriter.write(townPlan, OutputContext(Nil))
    println("try again")
    statefulWriter.write(townPlan, OutputContext(Nil))
  }
}
