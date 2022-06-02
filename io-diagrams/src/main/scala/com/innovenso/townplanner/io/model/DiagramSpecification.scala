package com.innovenso.townplanner.io.model

import com.innovenso.townplan.io.context.{OutputFileType, OutputType}
import com.innovenso.townplanner.io.context.SimpleDiagram
import com.innovenso.townplanner.model.language.{
  CompiledView,
  ModelComponent,
  TimelessView,
  View
}
import com.innovenso.townplanner.model.meta.{
  ADay,
  InTheFuture,
  InThePast,
  Today
}

case class DiagramSpecification(
    view: CompiledView[_ <: View],
    plantumlSpecification: String,
    relatedModelComponents: List[ModelComponent] = Nil,
    filenameAppendix: Option[String] = None,
    outputType: OutputType = SimpleDiagram
) {
  def assetName(fileType: OutputFileType): String =
    view.layer.name + "/" + view.groupTitle + "/" + pointInTimeDirectory + view.title + filenameAppendix
      .map(appendix => s" $appendix")
      .getOrElse("") + fileType.extension

  private def pointInTimeDirectory: String =
    view.pointInTimeName.getOrElse("") + "/"

  def title: String = view.title + filenameAppendix.map(" " + _).getOrElse("")
}
