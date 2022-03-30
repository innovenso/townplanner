package com.innovenso.townplanner.io.latex.model.tables

trait LatexTableRow {
  def cells: List[LatexTableCell]
  def print: String
  def numberOfColumns: Int = cells.map(_.colspan).sum
}

case class LatexBodyRow(cells: List[LatexTableCell]) extends LatexTableRow {
  val print: String = cells.map(_.print).mkString(" & ") + "\\\\"
}

case class LatexHeaderRow(cells: List[LatexTableCell]) extends LatexTableRow {
  val print: String = cells.map(_.print).mkString(" & ") + "\\\\\\midrule"
}

case class LatexFooterRow(cells: List[LatexTableCell]) extends LatexTableRow {
  val print: String = "\\midrule\\\\" + cells.map(_.print).mkString(" & ")
}

case class LatexSecondaryHeaderRow(cells: List[LatexTableCell])
    extends LatexTableRow {
  def columnStart(cell: LatexTableCell): Int =
    cells.splitAt(cells.indexOf(cell))._1.map(_.colspan).sum
  val linesSpans: List[Option[String]] = cells.map {
    case LatexEmptyCell => None
    case t: LatexTextCell =>
      Some(s"${columnStart(t) + 1}-${columnStart(t) + t.colspan}")
  }
  val midrule: String = linesSpans
    .filter(_.isDefined)
    .map(_.get)
    .map(it => s"\\cmidrule(lr){${it}}")
    .mkString
  val print: String = cells.map(_.print).mkString(" & ") + s"\\\\${midrule}"
}
