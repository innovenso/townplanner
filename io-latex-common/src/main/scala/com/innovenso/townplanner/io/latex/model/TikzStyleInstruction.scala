package com.innovenso.townplanner.io.latex.model

trait TikzStyleInstruction {
  def value: String
}

case class TikzStyleRef(value: String) extends TikzStyleInstruction

case object Rectangle extends TikzStyleInstruction {
  val value: String = "rectangle"
}

case object RoundedCorners extends TikzStyleInstruction {
  val value: String = "rounded corners"
}

case class Fill(color: String) extends TikzStyleInstruction {
  val value: String = s"fill=$color"
}

case class Opacity(level: Double) extends TikzStyleInstruction {
  val value: String = s"opacity=$level"
}

case class Draw(color: String) extends TikzStyleInstruction {
  val value: String = s"draw=$color"
}

case class Text(color: String) extends TikzStyleInstruction {
  val value: String = s"text=$color"
}

case object TextCentered extends TikzStyleInstruction {
  val value: String = "text centered"
}

case class MinimumHeight(mm: Double) extends TikzStyleInstruction {
  val value: String = s"minimum height=${mm}mm"
}

case class MinimumWidth(mm: Double) extends TikzStyleInstruction {
  val value: String = s"minimum width=${mm}mm"
}

case class TextWidth(mm: Double) extends TikzStyleInstruction {
  val value: String = s"text width=${mm}mm"
}

case class TextHeight(mm: Double) extends TikzStyleInstruction {
  val value: String = s"text height=${mm}mm"
}

case class TextDepth(mm: Double) extends TikzStyleInstruction {
  val value: String = s"text depth=${mm}mm"
}

case class LineWidth(mm: Double) extends TikzStyleInstruction {
  val value: String = s"line width=${mm}mm"
}

case class InnerSep(mm: Double) extends TikzStyleInstruction {
  val value: String = s"inner sep=${mm}mm"
}

case class InnerXSep(mm: Double) extends TikzStyleInstruction {
  val value: String = s"inner xsep=${mm}mm"
}

case class InnerYSep(mm: Double) extends TikzStyleInstruction {
  val value: String = s"inner ysep=${mm}mm"
}

case class OuterSep(mm: Double) extends TikzStyleInstruction {
  val value: String = s"outer sep=${mm}mm"
}

case class OuterXSep(mm: Double) extends TikzStyleInstruction {
  val value: String = s"outer xsep=${mm}mm"
}

case class OuterYSep(mm: Double) extends TikzStyleInstruction {
  val value: String = s"outer ysep=${mm}mm"
}

case class XShift(mm: Double) extends TikzStyleInstruction {
  val value: String = s"xshift=${mm}mm"
}

case class YShift(mm: Double) extends TikzStyleInstruction {
  val value: String = s"yshift=${mm}mm"
}

case class Fit(identifiers: List[String]) extends TikzStyleInstruction {
  val value: String = s"fit=${identifiers.map("(" + _ + ")").mkString(" ")}"
}

trait RelativePosition extends TikzStyleInstruction {
  def direction: String
  def distance: Option[Double]
  def of: String

  def value: String =
    s"${direction}=${distance.map(it => s"${it}mm ").getOrElse("")}of ${of}"
}
case class AboveOf(of: String, distance: Option[Double] = None)
    extends RelativePosition {
  val direction = "above"
}

case class BelowOf(of: String, distance: Option[Double] = None)
    extends RelativePosition {
  val direction = "below"
}

case class LeftOf(of: String, distance: Option[Double] = None)
    extends RelativePosition {
  val direction = "left"
}

case class RightOf(of: String, distance: Option[Double] = None)
    extends RelativePosition {
  val direction = "right"
}

trait Font extends TikzStyleInstruction {
  def latexFont: LatexFont
  def value: String = s"font=${latexFont.name}"
}

case object TinyFont extends Font {
  val latexFont: LatexFont = Tiny
}

case object ScriptFont extends Font {
  val latexFont: LatexFont = Script
}

case object FootnoteFont extends Font {
  val latexFont: LatexFont = Footnote
}

case object SmallFont extends Font {
  val latexFont: LatexFont = Small
}

case object NormalFont extends Font {
  val latexFont: LatexFont = Normal
}

case object LargeFont extends Font {
  val latexFont: LatexFont = Large
}

case object VeryLargeFont extends Font {
  val latexFont: LatexFont = VeryLarge
}

case object ExtremelyLargeFont extends Font {
  val latexFont: LatexFont = ExtremelyLarge
}

case object HugeFont extends Font {
  val latexFont: LatexFont = Huge
}

case object VeryHugeFont extends Font {
  val latexFont: LatexFont = VeryHuge
}

case class Rotate(degrees: Int) extends TikzStyleInstruction {
  val value: String = s"rotate=${degrees}"
}

case object Midway extends TikzStyleInstruction {
  val value: String = "midway"
}

case class Anchor(direction: CompassDirection) extends TikzStyleInstruction {
  val value: String = s"anchor=${direction.value}"
}

sealed trait CompassDirection {
  def value: String
}

case object North extends CompassDirection {
  val value = "north"
}

case object South extends CompassDirection {
  val value = "south"
}

case object East extends CompassDirection {
  val value = "east"
}

case object West extends CompassDirection {
  val value = "west"
}

case object NorthEast extends CompassDirection {
  val value = "north east"
}

case object NorthWest extends CompassDirection {
  val value = "north west"
}

case object SouthEast extends CompassDirection {
  val value = "south east"
}

case object SouthWest extends CompassDirection {
  val value = "south west"
}
