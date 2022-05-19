package com.innovenso.townplanner.io.openexchange.model

import com.innovenso.townplan.io.context.{
  Html,
  Output,
  OutputFileType,
  Success,
  Xml
}
import com.innovenso.townplan.repository.AssetRepository
import com.innovenso.townplanner.io.openexchange.context.OpenExchange
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.views.{
  CompiledFullTownPlanView,
  ExportView,
  SingleElementView
}
import com.innovenso.townplanner.model.language.Element
import com.innovenso.townplanner.model.meta.Today
import org.apache.commons.io.FileUtils

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.UUID
import scala.xml.{Elem, PrettyPrinter, XML}

case class TownPlanOpenExchangeExport(
    townPlan: TownPlan
)(implicit assetRepository: AssetRepository) {

  val xml: Elem =
    <model xmlns="http://www.opengroup.org/xsd/archimate/3.0/"
                   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xsi:schemaLocation="http://www.opengroup.org/xsd/archimate/3.0/ http://www.opengroup.org/xsd/archimate/3.1/archimate3_Model.xsd"
                   identifier="model-1"
                   version="1.0">
      <name xml:lang="en">Town Plan</name>
      <documentation xml:lang="en">Export from Innovenso Townplanner</documentation>

      <elements>
        {
      for (
        element <- townPlan
          .components(classOf[Element])
          .map(ElementOpenExchangeMapping)
          .filter(_.xml.isDefined)
          .map(_.xml.get)
      ) yield element
    }
      </elements>
    </model>

  val file: File = {
    val targetFile =
      File.createTempFile(UUID.randomUUID.toString, Xml.extension)
    FileUtils.writeStringToFile(
      targetFile,
      new PrettyPrinter(120, 4).format(xml),
      StandardCharsets.UTF_8
    )
    targetFile
  }

  val assetName: String =
    "exports/openexchange" + Xml.extension

  val output: Output = {
    assetRepository.write(file, assetName)
    Output(
      view = ExportView("OpenExchange Export"),
      relatedModelComponents = townPlan.enterprises,
      result = Success,
      assetName = Some(assetName),
      fileType = Xml,
      outputType = OpenExchange,
      day = Today
    )
  }

}
