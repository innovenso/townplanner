package com.innovenso.townplanner.io.latex

import com.innovenso.townplan.io.context.{
  Output,
  OutputContext,
  OutputType,
  Success
}
import com.innovenso.townplan.repository.FileSystemAssetRepository
import com.innovenso.townplanner.io.TownPlanDiagramWriter
import com.innovenso.townplanner.io.latex.model.LatexSpecification
import com.innovenso.townplanner.model.language.{
  CompiledView,
  ModelComponent,
  View
}
import com.innovenso.townplanner.model.meta.{
  ADay,
  ApplicationLayer,
  Key,
  Layer,
  ModelComponentType,
  SortKey,
  Today
}
import com.innovenso.townplanner.model.samples.SampleFactory
import com.innovenso.townplanner.model.{EnterpriseArchitecture, TownPlan}

import java.io.File
import java.nio.file.Files
import scala.io.Source

trait LatexIO {
  val ea = new EnterpriseArchitecture()
  val targetDirectory: File =
    Files.createTempDirectory("TownplannerLatex").toFile
  val assetDirectory = new File(targetDirectory, "assets")
  val assetRepository = new FileSystemAssetRepository(assetDirectory.toPath)
  val samples: SampleFactory = SampleFactory(ea)
  val townPlanDiagramWriter: TownPlanDiagramWriter =
    TownPlanDiagramWriter(targetDirectory.toPath, assetRepository)

  def townPlan: TownPlan = ea.townPlan

  def diagramsAreWritten(viewKey: Key): OutputContext = {
    townPlanDiagramWriter
      .write(townPlan, viewKey.value, OutputContext(Nil))
  }

  def pdfIsWritten(source: String): List[Output] = {
    LatexPdfWriter(
      LatexSpecification(
        view = CompiledTestView(),
        latexSourceCode = source,
        outputType = TestOutput
      ),
      assetRepository,
      OutputContext(Nil)
    ).document
  }

  def assetsExistWhen(outputs: List[Output]): Boolean = {
    outputs.foreach(it => println(it.assetName.getOrElse("unknown asset name")))
    assetRepository.objectNames.foreach(it =>
      println(
        assetRepository
          .read(it)
          .map(_.getAbsolutePath)
          .getOrElse("unknown file")
      )
    )
    outputs.forall(it => it.result == Success) && assetRepository.objectNames
      .forall(assetRepository.read(_).exists(_.canRead))

  }

}

case object TestOutput extends OutputType {
  override val title: String = "Test Output"

  override val description: String = "Output Type purely used by unit tests"
}
case class TestView() extends View {
  override val pointInTime: ADay = Today

  override val layer: Layer = ApplicationLayer

  override val title: String = "Test View"

  override val key: Key = Key()

  override val sortKey: SortKey = SortKey.next

  override val modelComponentType: ModelComponentType = ModelComponentType(
    "Test View"
  )
}

case class CompiledTestView() extends CompiledView[TestView] {
  override val view: TestView = TestView()

  override val title: String = view.title

  override val groupTitle: String = "Test"

  override val modelComponents: Map[Key, ModelComponent] = Map()
}
