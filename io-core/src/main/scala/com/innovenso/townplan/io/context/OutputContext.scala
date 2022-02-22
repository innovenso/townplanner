package com.innovenso.townplan.io.context

case class OutputContext(outputs: List[Output]) {
  def withOutputs(outputsToAdd: List[Output]): OutputContext = copy(outputsToAdd ::: outputs)
  def withOutput(outputToAdd: Output): OutputContext = copy(outputToAdd :: outputs)
}
