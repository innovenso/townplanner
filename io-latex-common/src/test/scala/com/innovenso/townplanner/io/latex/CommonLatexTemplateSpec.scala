package com.innovenso.townplanner.io.latex

import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec
import play.twirl.api.Txt
import txt.{Command, Environment, Frame, Preamble, Section, UsePackage}

import java.time.LocalDate
import scala.reflect.internal.util.NoSourceFile.content

class CommonLatexTemplateSpec extends AnyFlatSpec with GivenWhenThen {
  "A LaTeX command" should "be rendered properly" in {
    When("a LaTeX command is rendered")
    val command1 = Command("document").body
    val command2 =
      Command("section", List("param1", "param2"), List("required1")).body
    Then("it is rendered correctly")
    assert(command1 == "\\document")
    assert(command2 == "\\section[param1][param2]{required1}")
  }

  "An environment" should "be rendered properly" in {
    val document =
      Environment(name = "section", parameters = Nil)(content = Txt(""))
    println(document.body)
  }

  "A preamble" should "be rendered properly" in {
    val preamble = Preamble(
      institute = Some("Innovenso BV ë {}"),
      title = Some("$-)lqksdjfëöä")
    )
    println(preamble.body)
  }

  "A package" should "be useable" in {
    val p = UsePackage(name = "beamer")
    println(p.body)
    val p2 =
      UsePackage(name = "beamer", parameters = Some("ab,cd"), forTheme = true)
    println(p2.body)
  }

  "A section" should "render correctly" in {
    val s = Section(title = "This is a sëction")(content = Txt("hello"))
    println(s)
  }

  "A frame" should "render correctly" in {
    val f =
      Frame(title = "This is a främe!", subtitle = Some("With $subtitle%"))(
        content = Txt("hello")
      )
    println(f.body)
  }
}
