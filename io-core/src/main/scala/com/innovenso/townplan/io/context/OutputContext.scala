package com.innovenso.townplan.io.context

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
}
