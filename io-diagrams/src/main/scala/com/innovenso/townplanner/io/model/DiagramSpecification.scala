package com.innovenso.townplanner.io.model

import com.innovenso.townplan.io.context.OutputFileType
import com.innovenso.townplanner.model.language.{CompiledView, View}
import com.innovenso.townplanner.model.meta.{ADay, Today}

case class DiagramSpecification(
    view: CompiledView[_ <: View],
    plantumlSpecification: String
) {
  def assetName(fileType: OutputFileType): String =
    view.layer.name + "/" + view.key.value + "/" + pointInTimeName(
      view.pointInTime
    ) + "/" + view.title + fileType.extension

  def pointInTimeName(day: ADay): String =
    if (day == Today) "Today"
    else
      s"${day.year}-${day.month}-${day.day}"
}