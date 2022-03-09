package com.innovenso.townplan.application

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplan.repository.FileSystemAssetRepository
import com.innovenso.townplanner.io.{
  TownPlanDiagramWriter,
  TownPlanTechnologyRadarWriter
}
import com.innovenso.townplanner.model.{EnterpriseArchitecture, TownPlan}

import java.io.File

trait EnterpriseArchitectureAsCode extends App {
  implicit val ea: EnterpriseArchitecture = EnterpriseArchitecture()
  val targetDirectory: File = new File("output")
  val assetDirectory = new File(targetDirectory, "assets")
  val assetRepository: FileSystemAssetRepository = FileSystemAssetRepository(
    assetDirectory.toPath
  )

  val townPlanDiagramWriter: TownPlanDiagramWriter =
    TownPlanDiagramWriter(targetDirectory.toPath, assetRepository)
  val townPlanTechnologyRadarWriter: TownPlanTechnologyRadarWriter =
    TownPlanTechnologyRadarWriter(assetRepository)

  def townPlan: TownPlan = ea.townPlan

  implicit var outputContext: OutputContext = OutputContext(Nil)
  def diagrams()(implicit outputContext: OutputContext): Unit =
    this.outputContext = townPlanDiagramWriter.write(townPlan, outputContext)

  def technologyRadar(
      title: String = "Technology Radar"
  )(implicit outputContext: OutputContext): Unit = this.outputContext =
    townPlanTechnologyRadarWriter.write(townPlan, outputContext, Some(title))
}
