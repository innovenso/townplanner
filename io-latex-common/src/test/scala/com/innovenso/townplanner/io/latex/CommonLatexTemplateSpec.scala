package com.innovenso.townplanner.io.latex

import com.innovenso.townplan.io.context.Output
import com.innovenso.townplanner.io.latex.model.tables.{
  ColumnCenter,
  ColumnLeft,
  ColumnParagraph,
  ColumnRight,
  LatexBodyRow,
  LatexEmptyCell,
  LatexHeaderRow,
  LatexSecondaryHeaderRow,
  LatexTable,
  LatexTextCell
}
import com.innovenso.townplanner.io.latex.model.{
  Bold,
  Fill,
  InnerSep,
  Rectangle,
  Uppercase,
  VeryHugeFont
}
import com.innovenso.townplanner.io.latex.test.LatexIO
import latex.lib.tables.txt.Table
import latex.lib.techradar.txt.RadarPositionPicture
import latex.lib.tikz.txt.{TikzDocument, TikzStyle}
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec
import play.twirl.api.{Txt, TxtFormat}
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

  "Text Variants" should "be applied correctly" in {
    val original = "This is a text"
    val variants = List(Bold, Uppercase)
    assert(
      LatexFormat.apply(
        variants,
        original
      ) == "\\uppercase{\\textbf{This is a text}}"
    )
  }

  "An environment" should "be rendered properly" in {
    val document =
      Environment(
        name = "section",
        parameters = List("frame"),
        optionalParameters = List("hello")
      )(content = Txt(""))
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

  "A TikZ style" should "render correctly" in {
    val s = TikzStyle(
      "block",
      List(Rectangle, Fill("green"), VeryHugeFont, InnerSep(0.5))
    )
    println(s.body)
  }

  "A radar position drawing" should "output TikZ syntax" in new LatexIO {
    val p = RadarPositionPicture(technology = samples.technique)
    println(p.body)
  }

  "A radar position drawing" should "render correctly" in new LatexIO {
    assert(
      assetsExistWhen(
        pdfIsWritten(
          TikzDocument("Radar Position", townPlan)(
            RadarPositionPicture(technology = samples.technique)
          ).body
        )
      )
    )
  }

  "A table" should "output correct syntax" in new LatexIO {
    val headerRow: LatexHeaderRow = LatexHeaderRow(
      List(
        LatexEmptyCell,
        LatexTextCell(samples.title),
        LatexTextCell(samples.title),
        LatexTextCell(samples.title)
      )
    )
    val secondaryHeaderRow: LatexSecondaryHeaderRow = LatexSecondaryHeaderRow(
      List(
        LatexEmptyCell,
        LatexTextCell(samples.title, colspan = 2),
        LatexTextCell(samples.title)
      )
    )
    val bodyRows: List[LatexBodyRow] = (1 to 10)
      .map(_ =>
        LatexBodyRow((1 to 4).map(_ => LatexTextCell(samples.title)).toList)
      )
      .toList
    val table: LatexTable =
      LatexTable(
        List(ColumnParagraph(50), ColumnCenter, ColumnCenter, ColumnRight),
        secondaryHeaderRow :: headerRow :: bodyRows
      )
    val output: TxtFormat.Appendable = Table(table, title = Some(samples.title))
    println(output.body)
    assert(
      assetsExistWhen(
        pdfIsWritten(
          TikzDocument("Table", townPlan)(output).body
        )
      )
    )
  }
}
