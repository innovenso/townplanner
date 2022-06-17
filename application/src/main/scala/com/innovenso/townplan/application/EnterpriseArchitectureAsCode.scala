package com.innovenso.townplan.application

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplan.io.state.FileSystemStateRepository
import com.innovenso.townplan.repository.FileSystemAssetRepository
import com.innovenso.townplanner.io.latex.document.TownPlanDocumentWriter
import com.innovenso.townplanner.io.latex.picture.TownPlanPictureWriter
import com.innovenso.townplanner.io.latex.slides.TownPlanSlideDeckWriter
import com.innovenso.townplanner.io.openexchange.TownPlanOpenExchangeWriter
import com.innovenso.townplanner.io.{
  TownPlanDiagramWriter,
  TownPlanWebsiteWriter
}
import com.innovenso.townplanner.model.{EnterpriseArchitecture, TownPlan}

import java.io.File

trait EnterpriseArchitectureAsCode extends App {
  implicit val ea: EnterpriseArchitecture = EnterpriseArchitecture()
  val targetDirectory: File = new File("output")
  val assetDirectory = new File(targetDirectory, "assets")
  val stateDirectory = new File(targetDirectory, "state")
  implicit val assetRepository: FileSystemAssetRepository =
    FileSystemAssetRepository(
      assetDirectory.toPath
    )
  implicit val stateRepository: FileSystemStateRepository =
    FileSystemStateRepository(stateDirectory.toPath)

  val townPlanDiagramWriter: TownPlanDiagramWriter =
    TownPlanDiagramWriter(
      targetDirectory.toPath,
      assetRepository,
      stateRepository
    )
  val websiteWriter: TownPlanWebsiteWriter =
    TownPlanWebsiteWriter()

  val openExchangeWriter: TownPlanOpenExchangeWriter =
    TownPlanOpenExchangeWriter()

  val townPlanDocumentWriter: TownPlanDocumentWriter = TownPlanDocumentWriter(
    assetRepository,
    stateRepository
  )
  val townPlanSlideDeckWriter: TownPlanSlideDeckWriter =
    TownPlanSlideDeckWriter(assetRepository, stateRepository)

  val townPlanPictureWriter: TownPlanPictureWriter = TownPlanPictureWriter(
    assetRepository,
    stateRepository
  )

  def townPlan: TownPlan = ea.townPlan

  implicit var outputContext: OutputContext = OutputContext(Nil)

  def diagrams()(implicit outputContext: OutputContext): Unit =
    this.outputContext = townPlanDiagramWriter.write(townPlan, outputContext)

  @deprecated("the technology radar website is replaced by the website render")
  def technologyRadarWebsite()(implicit outputContext: OutputContext): Unit =
    println("the technology radar website is replaced by the website render")

  def website()(implicit outputContext: OutputContext): Unit =
    this.outputContext = websiteWriter.write()(townPlan, outputContext)

  def archimate()(implicit outputContext: OutputContext): Unit =
    this.outputContext = openExchangeWriter.write()(townPlan, outputContext)

  def documents()(implicit outputContext: OutputContext): Unit = {
    this.outputContext = townPlanPictureWriter.write(townPlan, outputContext)
    this.outputContext =
      townPlanDocumentWriter.write(townPlan, this.outputContext)
    this.outputContext =
      townPlanSlideDeckWriter.write(townPlan, this.outputContext)
  }
}
