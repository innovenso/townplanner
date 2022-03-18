package com.innovenso.townplanner.io.latex.model

import com.innovenso.townplan.io.context.{
  Output,
  OutputFileType,
  OutputType,
  Pdf
}
import com.innovenso.townplanner.model.language.{
  CompiledView,
  TimelessView,
  View
}
import com.innovenso.townplanner.model.meta.{
  ADay,
  InTheFuture,
  InThePast,
  Today
}

case class LatexSpecification(
    view: CompiledView[_ <: View],
    latexSourceCode: String,
    dependencies: List[Output] = Nil,
    filenameAppendix: Option[String] = None,
    outputType: OutputType
) {
  val assetName: String =
    view.layer.name + "/" + view.groupTitle + "/" + pointInTimeDirectory + view.title + filenameAppendix
      .map(appendix => s" $appendix")
      .getOrElse("") + s" ${outputType.title}" + Pdf.extension

  private def pointInTimeDirectory: String =
    if (view.view.isInstanceOf[TimelessView]) ""
    else pointInTimeName(view.pointInTime) + "/"

  private def pointInTimeName(day: ADay): String =
    if (day == Today) "As Is Today"
    else if (day == InThePast) "As Was"
    else if (day == InTheFuture) "To Be"
    else if (day.isBefore(Today))
      s"As Was on ${day.year}-${day.month}-${day.day}"
    else
      s"To Be on ${day.year}-${day.month}-${day.day}"

}
