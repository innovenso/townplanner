package com.wayneenterprises.townplan

import com.innovenso.townplan.io.context.{OutputContext, Success}
import com.innovenso.townplan.repository.FileSystemAssetRepository
import com.innovenso.townplanner.io.{
  DiagramSpecificationWriter,
  TownPlanDiagramWriter
}
import com.innovenso.townplanner.io.model.DiagramSpecification
import com.innovenso.townplanner.model.language.{CompiledView, View}
import com.innovenso.townplanner.model.meta.Key
import com.innovenso.townplanner.model.{EnterpriseArchitecture, TownPlan}

import java.io.File
import java.nio.file.Files

trait DiagramIO {
  implicit val ea: EnterpriseArchitecture = new EnterpriseArchitecture()
  val targetDirectory: File =
    Files.createTempDirectory("TownplannerDiagrams").toFile
  val assetDirectory = new File(targetDirectory, "assets")
  val assetRepository = new FileSystemAssetRepository(assetDirectory.toPath)

  val townPlanDiagramWriter: TownPlanDiagramWriter =
    TownPlanDiagramWriter(targetDirectory.toPath, assetRepository)
  val outputContext: OutputContext =
    townPlanDiagramWriter.write(townPlan, OutputContext(Nil))

  def specificationExists(view: Option[CompiledView[_ <: View]]): Boolean = {
    val specs = specifications(view)
    specs.nonEmpty
  }

  def specifications(
      view: Option[CompiledView[_ <: View]]
  ): List[DiagramSpecification] = {
    view
      .map(DiagramSpecificationWriter.specifications(_)(townPlan))
      .getOrElse(Nil)
  }

  def diagramsAreWritten(viewKey: Key): Boolean = {
    val outputs = townPlanDiagramWriter
      .write(townPlan, viewKey.value, OutputContext(Nil))
      .outputs
    outputs.foreach(output =>
      println(assetRepository.read(output.assetName.get).get.getAbsolutePath)
    )
    outputs.nonEmpty && outputs.forall(_.result.equals(Success))
  }

  def townPlan: TownPlan = ea.townPlan

}
