package com.innovenso.townplanner.io.openexchange

import com.innovenso.townplan.io.context.{OutputContext, Success}
import com.innovenso.townplan.repository.{
  AssetRepository,
  FileSystemAssetRepository
}
import com.innovenso.townplanner.io.openexchange.model.TownPlanOpenExchangeExport
import com.innovenso.townplanner.model.concepts.views.{
  CompiledFullTownPlanView,
  FullTownPlanView
}
import com.innovenso.townplanner.model.language.{CompiledView, Element, View}
import com.innovenso.townplanner.model.meta.Key
import com.innovenso.townplanner.model.samples.SampleFactory
import com.innovenso.townplanner.model.{EnterpriseArchitecture, TownPlan}

import java.io.File
import java.nio.file.Files
import scala.xml.Elem

trait OpenExchangeIO {
  val ea = new EnterpriseArchitecture()
  val targetDirectory: File =
    Files.createTempDirectory("TownplannerOpenExchange").toFile
  val assetDirectory = new File(targetDirectory, "assets")
  implicit val assetRepository: AssetRepository =
    new FileSystemAssetRepository(
      assetDirectory.toPath
    )
  val samples: SampleFactory = SampleFactory(ea)
  val outputContext: OutputContext = OutputContext(Nil)
  val writer: TownPlanOpenExchangeWriter = TownPlanOpenExchangeWriter()

  def townPlan: TownPlan = ea.townPlan

  def writeOpenExchange: OutputContext = {
    val oc = writer.write()(townPlan, outputContext)
    assetRepository.objectNames
      .map(assetRepository.read)
      .filter(_.nonEmpty)
      .map(_.get)
      .foreach(f => println(f.getAbsolutePath))
    oc
  }

  def xml: Option[Elem] = Some(
    TownPlanOpenExchangeExport(townPlan).xml
  )

  def xmlContainsElement(
      element: Element
  ): Boolean = {
    xml
      .map(_ \ "elements")
      .map(_ \ "element")
      .map(_ \ "name")
      .map(_.text)
      .exists(_.contains(element.title))
  }
}
