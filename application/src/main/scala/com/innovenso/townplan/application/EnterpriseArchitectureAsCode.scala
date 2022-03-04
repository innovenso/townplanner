package com.innovenso.townplan.application

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplan.repository.FileSystemAssetRepository
import com.innovenso.townplanner.io.TownPlanDiagramWriter
import com.innovenso.townplanner.model.{TownPlan, EnterpriseArchitecture}

import java.io.File

trait EnterpriseArchitectureAsCode extends App {
  implicit val ea: EnterpriseArchitecture = EnterpriseArchitecture()
  val targetDirectory: File = new File("output")
  val assetDirectory = new File(targetDirectory, "assets")
  val assetRepository: FileSystemAssetRepository = FileSystemAssetRepository(assetDirectory.toPath)

  val townPlanDiagramWriter: TownPlanDiagramWriter =
    TownPlanDiagramWriter(targetDirectory.toPath, assetRepository)

  def townPlan: TownPlan = ea.townPlan

  def diagrams: OutputContext = diagrams(OutputContext(Nil))
  def diagrams(outputContext: OutputContext): OutputContext = townPlanDiagramWriter.write(townPlan, outputContext)
}
