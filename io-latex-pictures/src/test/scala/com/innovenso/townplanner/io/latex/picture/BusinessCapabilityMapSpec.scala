package com.innovenso.townplanner.io.latex.picture

import com.innovenso.townplanner.io.latex.model.{BelowOf, RightOf}
import com.innovenso.townplanner.io.latex.test.LatexIO
import com.innovenso.townplanner.model.concepts.{BusinessCapability, Enterprise}
import com.innovenso.townplanner.model.concepts.views.{
  BusinessCapabilityMap,
  CompiledBusinessCapabilityMap
}
import com.innovenso.townplanner.model.meta.Key
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec
import tikz.txt.BusinessCapabilityMapPicture

class BusinessCapabilityMapSpec extends AnyFlatSpec with GivenWhenThen {

  "Capabilities" should "have a correct relative position" in new LatexIO {
    Given("A business capability map")
    val enterprise: Enterprise =
      ea has Enterprise(title = "Innovenso", key = Key("innovenso"))
    val level01: BusinessCapability = ea describes BusinessCapability(
      title = "level01",
      key = Key("level01")
    ) as { it =>
      it serves enterprise
    }
    val level02: BusinessCapability = ea describes BusinessCapability(
      title = "level02",
      key = Key("level02")
    ) as { it =>
      it serves enterprise
    }
    val level03: BusinessCapability = ea describes BusinessCapability(
      title = "level03",
      key = Key("level03")
    ) as { it =>
      it serves enterprise
    }
    val level111: BusinessCapability = ea describes BusinessCapability(
      title = "level111",
      key = Key("level111")
    ) as { it =>
      it serves level01
    }
    val level112: BusinessCapability = ea describes BusinessCapability(
      title = "level112",
      key = Key("level112")
    ) as { it =>
      it serves level01
    }
    val level113: BusinessCapability = ea describes BusinessCapability(
      title = "level113",
      key = Key("level113")
    ) as { it =>
      it serves level01
    }
    val level1121: BusinessCapability = ea describes BusinessCapability(
      title = "level1121",
      key = Key("level1121")
    ) as { it =>
      it serves level112
    }
    val level1122: BusinessCapability = ea describes BusinessCapability(
      title = "level1122",
      key = Key("level1122")
    ) as { it =>
      it serves level112
    }
    val level1123: BusinessCapability = ea describes BusinessCapability(
      title = "level1123",
      key = Key("level1123")
    ) as { it =>
      it serves level112
    }
    val level121: BusinessCapability = ea describes BusinessCapability(
      title = "level121",
      key = Key("level121")
    ) as { it =>
      it serves level02
    }
    val level1211: BusinessCapability = ea describes BusinessCapability(
      title = "level1211",
      key = Key("level1211")
    ) as { it =>
      it serves level121
    }

    val view: BusinessCapabilityMap =
      ea needs BusinessCapabilityMap(forEnterprise = enterprise)
    val compiledView: CompiledBusinessCapabilityMap =
      townPlan.businessCapabilityMap(view.key).get
    When("we have a position helper")
    val helper: BusinessCapabilityLayoutHelper =
      BusinessCapabilityLayoutHelper(compiledView)
    Then("The first level 0 capability has no relative position")
    assert(helper.positionInstruction(level01).isEmpty)
    And("The second level 0 capability is right of the first")
    assert(
      helper
        .positionInstruction(level02)
        .contains(RightOf(level01.key.camelCased + ".south east"))
    )
    And("The third level 0 capability is right of the second")
    assert(
      helper
        .positionInstruction(level03)
        .contains(
          RightOf(level02.key.camelCased + ".south east")
        )
    )
    And("The first level 1 capability is below the level 0 title")
    assert(
      helper
        .positionInstruction(level111)
        .contains(
          BelowOf(level01.key.camelCased, Some(2.5))
        )
    )
    And(
      "The second level 1 capability is below the first level 1 capability title if the first level 1 has no children"
    )
    assert(
      helper
        .positionInstruction(level112)
        .contains(
          BelowOf(level111.key.camelCased, Some(5))
        )
    )
    And(
      "The third level 1 capability is below the last of the level 2 capabilities belonging to the preceding level 1 capability"
    )
    assert(
      helper
        .positionInstruction(level113)
        .contains(BelowOf(level1123.key.camelCased, Some(5)))
    )
    And(
      "The first level 2 capability is below the title of it's level 1 parent capability"
    )
    assert(
      helper
        .positionInstruction(level1121)
        .contains(BelowOf(level112.key.camelCased))
    )
    And("The second level 2 capability is below the first level 2 capability")
    assert(
      helper
        .positionInstruction(level1122)
        .contains(BelowOf(level1121.key.camelCased))
    )
    And("The last child of a level 0 is the last level 1 if it has no level 2")
    assert(helper.lastChild(level01).contains(level113))
    And("The last child of a level 0 is the last level 2 of the last level 1")
    assert(helper.lastChild(level02).contains(level1211))
  }

  "Background colors" should "be assigned to business capabilities of level 0" in new LatexIO {
    val enterprise: Enterprise = samples.enterprise
    (1 to 40).foreach(_ => samples.capability(Some(enterprise)))
    val view: BusinessCapabilityMap =
      ea needs BusinessCapabilityMap(forEnterprise = enterprise)

    val compiledView: CompiledBusinessCapabilityMap =
      townPlan.businessCapabilityMap(view.key).get

    val helper: BusinessCapabilityLayoutHelper =
      BusinessCapabilityLayoutHelper(compiledView)

    assert(
      compiledView.level0BusinessCapabilities
        .forall(b => {
          val instruction = helper.backgroundColorInstruction(b)
          println(instruction)
          instruction.isDefined
        })
    )
  }

  "A business capability map" should "output TikZ syntax" in new LatexIO {
    Given("a business capability map")
    val enterprise: Enterprise = samples.enterprise
    (1 to 3).foreach(_ => {
      val level0 = samples.capability(Some(enterprise))
      (1 to samples.randomInt(10)).foreach(_ => {
        val level1 = samples.capability(parentCapability = Some(level0))
        (1 to samples.randomInt(10)).foreach(_ => {
          val tags = (1 to samples.randomInt(10)).toList.map(_ => samples.tag)
          samples.capability(parentCapability = Some(level1), tags = tags)
        })
      })
    })
    val view: BusinessCapabilityMap =
      ea needs BusinessCapabilityMap(forEnterprise = enterprise)

    val compiledView: CompiledBusinessCapabilityMap =
      townPlan.businessCapabilityMap(view.key).get
    val b =
      BusinessCapabilityMapPicture(townPlan,
        compiledView,
        BusinessCapabilityLayoutHelper(compiledView)
      )
    println(b.body)
  }

  "A business capability map" should "render correctly" in new LatexIO {
    val enterprise: Enterprise = samples.enterprise

    (1 to 20).foreach(_ => {
      val level0 = samples.capability(Some(enterprise))
      (1 to samples.randomInt(10)).foreach(_ => {
        val level1 = samples.capability(parentCapability = Some(level0))
        (1 to samples.randomInt(10)).foreach(_ => {
          val tags = (1 to samples.randomInt(4)).toList.map(_ => samples.tag)
          val level2 = samples.capability(parentCapability = Some(level1), tags = tags)
        })
      })
    })
    val view: BusinessCapabilityMap =
      ea needs BusinessCapabilityMap(forEnterprise = enterprise)
    val compiledView: CompiledBusinessCapabilityMap =
      townPlan.businessCapabilityMap(view.key).get

    assert(
      assetsExistWhen(
        pdfIsWritten(
          BusinessCapabilityMapPicture(
            townPlan,
            compiledView,
            BusinessCapabilityLayoutHelper(compiledView)
          ).body
        )
      )
    )
  }

}
