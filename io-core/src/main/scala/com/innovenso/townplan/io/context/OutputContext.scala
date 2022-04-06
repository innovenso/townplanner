package com.innovenso.townplan.io.context

import com.innovenso.townplanner.model.language.{ModelComponent, View}

case class OutputContext(outputs: List[Output]) {
  def withOutputs(outputsToAdd: List[Output]): OutputContext = copy(
    outputsToAdd ::: outputs
  )
  def withOutput(outputToAdd: Output): OutputContext = copy(
    outputToAdd :: outputs
  )

  def outputsOfFileType(fileType: OutputFileType): List[Output] = outputs
    .filter(output => output.result == Success)
    .filter(output => output.fileType == fileType)

  def outputsOfType(
      fileType: OutputFileType,
      outputType: OutputType
  ): List[Output] =
    outputsOfFileType(fileType).filter(output =>
      output.outputType == outputType
    )

  def outputs(ofFileType: Option[OutputFileType] = None, ofOutputType: Option[OutputType] = None, forView: Option[View] = None, forModelComponents: List[ModelComponent] = Nil): List[Output] = {
    outputs.filter(output => output.result == Success)
      .filter(output => ofFileType.forall(output.fileType.equals(_)))
      .filter(output => ofOutputType.forall(output.outputType.equals(_)))
      .filter(output => forView.forall(output.view.equals(_)))
      .filter(output => forModelComponents.forall(output.relatedModelComponents.contains(_)))
  }
}
