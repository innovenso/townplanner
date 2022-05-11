package com.innovenso.townplanner.io

import com.innovenso.townplan.io.context.{MarkDown, OutputContext}
import com.innovenso.townplan.repository.{AssetRepository, FileSystemAssetRepository}
import com.innovenso.townplanner.model.concepts.views.TechnologyRadar
import com.innovenso.townplanner.model.language.Element
import com.innovenso.townplanner.model.meta.Key
import com.innovenso.townplanner.model.samples.SampleFactory
import com.innovenso.townplanner.model.{EnterpriseArchitecture, TownPlan}

import java.io.File
import java.nio.file.Files
import scala.io.Source

trait WebsiteIO {
  val ea = new EnterpriseArchitecture()
  val targetDirectory: File =
    Files.createTempDirectory("TownplannerWebsite").toFile
  val assetDirectory = new File(targetDirectory, "assets")
  implicit val assetRepository: AssetRepository = new FileSystemAssetRepository(assetDirectory.toPath)
  val samples: SampleFactory = SampleFactory(ea)
  val diagramWriter: TownPlanDiagramWriter =
    TownPlanDiagramWriter(targetDirectory.toPath, assetRepository)

  def diagramsAreWritten: OutputContext = {
    diagramWriter
      .write(townPlan, OutputContext(Nil))
  }

  val websiteWriter: TownPlanWebsiteWriter =
    TownPlanWebsiteWriter()(assetRepository)

  def townPlan: TownPlan = ea.townPlan

  def markdownFileExists(inSourceDirectory: File, element: Element): Boolean = {
    val layerDirectory = new File(inSourceDirectory, element.layer.name)
    val componentTypeDirectory = new File(layerDirectory, element.modelComponentType.value)
    val markdownFile = new File(componentTypeDirectory, element.title + MarkDown.extension)
    markdownFile.canRead
  }
}
