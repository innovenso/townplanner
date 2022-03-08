package com.innovenso.townplanner.io.model

import com.innovenso.townplan.io.context.OutputFileType
import com.innovenso.townplanner.model.language.{CompiledView, View}
import com.innovenso.townplanner.model.meta.{
  ADay,
  InTheFuture,
  InThePast,
  Today
}

case class DiagramSpecification(
    view: CompiledView[_ <: View],
    plantumlSpecification: String,
    filenameAppendix: Option[String] = None
) {
  def assetName(fileType: OutputFileType): String =
    view.layer.name + "/" + view.groupTitle + "/" + pointInTimeName(
      view.pointInTime
    ) + "/" + view.title + filenameAppendix
      .map(appendix => s" $appendix")
      .getOrElse("") + fileType.extension

  def pointInTimeName(day: ADay): String =
    if (day == Today) "As Is Today"
    else if (day == InThePast) "As Was"
    else if (day == InTheFuture) "To Be"
    else if (day.isBefore(Today))
      s"As Was on ${day.year}-${day.month}-${day.day}"
    else
      s"To Be on ${day.year}-${day.month}-${day.day}"
}
