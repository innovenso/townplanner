package com.innovenso.townplanner.io.latex.document

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplan.repository.FileSystemAssetRepository
import com.innovenso.townplanner.io.TownPlanDiagramWriter
import com.innovenso.townplanner.io.latex.picture.TownPlanPictureWriter
import com.innovenso.townplanner.model.concepts.{Enterprise, EnterpriseConfigurer}
import com.innovenso.townplanner.model.meta.Key
import com.innovenso.townplanner.model.samples.SampleFactory
import com.innovenso.townplanner.model.{EnterpriseArchitecture, TownPlan}

import java.io.File
import java.nio.file.Files

trait LatexDocumentIO {
  val ea = new EnterpriseArchitecture()
  val targetDirectory: File =
    Files.createTempDirectory("TownplannerLatex").toFile
  val assetDirectory = new File(targetDirectory, "assets")
  val assetRepository = new FileSystemAssetRepository(assetDirectory.toPath)
  val samples: SampleFactory = SampleFactory(ea)
  val townPlanDiagramWriter: TownPlanDiagramWriter =
    TownPlanDiagramWriter(targetDirectory.toPath, assetRepository)

  val townPlanPictureWriter: TownPlanPictureWriter = TownPlanPictureWriter(assetRepository)
  val townPlanDocumentWriter: TownPlanDocumentWriter = TownPlanDocumentWriter(
    assetRepository
  )

  def townPlan: TownPlan = ea.townPlan

  def diagramsAreWritten(viewKey: Key): OutputContext = {
    townPlanDiagramWriter
      .write(townPlan, viewKey.value, OutputContext(Nil))
  }

  def picturesAreWritten(viewKey: Key, outputContext: OutputContext = OutputContext(Nil)): OutputContext = {
    townPlanPictureWriter.write(townPlan, viewKey.value, OutputContext(Nil))
  }

  def documentsAreWritten(viewKey: Key, outputContext: OutputContext = OutputContext(Nil)): OutputContext = {
    townPlanDocumentWriter.write(townPlan, viewKey.value, outputContext)
  }

}
