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
import com.innovenso.townplanner.model.meta.{Key, Layer, Today}
import org.apache.commons.io.FileUtils

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.UUID
import scala.xml.{Elem, PrettyPrinter, XML}

case class TownPlanOpenExchangeExport(
    townPlan: TownPlan
)(implicit assetRepository: AssetRepository) {

  val elementMappings: Map[Key, ElementOpenExchangeMapping] = townPlan
    .components(classOf[Element])
    .map(ElementOpenExchangeMapping)
    .filter(_.xml.isDefined)
    .map(m => (m.element.key, m))
    .toMap
  val relationshipMappings: Map[Key, RelationshipOpenExchangeMapping] =
    townPlan.relationships
      .filter(r => r.participants.forall(p => elementMappings.contains(p)))
      .map(RelationshipOpenExchangeMapping)
      .filter(_.xml.isDefined)
      .map(m => (m.relationship.key, m))
      .toMap
  def layerElementMappings(layer: Layer): Map[Key, ElementOpenExchangeMapping] =
    elementMappings.filter(_._2.element.layer.equals(layer))
  val layers: Map[Layer, Map[Key, ElementOpenExchangeMapping]] =
    Layer.values.map(layer => (layer, layerElementMappings(layer))).toMap

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
      for (element <- elementMappings.values.map(_.xml.get)) yield element
    }
      </elements>
      <relationships>
      {
      for (
        relationship <- relationshipMappings.values
          .map(_.xml.get)
      ) yield relationship
    }
      </relationships>
      <organizations>
        {
      for (
        layerItem <- layers
          .map(t => LayerOrganisationOpenExchangeMapping(t._1, t._2))
          .map(_.xml)
      )
        yield layerItem
    }
      </organizations>
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
