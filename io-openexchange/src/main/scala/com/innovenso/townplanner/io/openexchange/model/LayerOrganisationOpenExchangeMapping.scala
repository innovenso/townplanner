package com.innovenso.townplanner.io.openexchange.model

import com.innovenso.townplanner.model.language.Element
import com.innovenso.townplanner.model.meta.{Key, Layer, ModelComponentType}

import scala.xml.Elem

case class LayerOrganisationOpenExchangeMapping(
    layer: Layer,
    elements: Map[Key, ElementOpenExchangeMapping]
) {
  val modelComponentTypes: List[String] = elements.values
    .map(_.element)
    .map(_.modelComponentType.value)
    .toList
    .distinct
  def elementsOfType(modelComponentType: String): List[Element] =
    elements.values
      .map(_.element)
      .filter(_.modelComponentType.value.equals(modelComponentType))
      .toList
  val groups: List[ModelComponentTypeOrganisationOpenExchangeMapping] =
    modelComponentTypes.map(mct =>
      ModelComponentTypeOrganisationOpenExchangeMapping(
        mct,
        elementsOfType(mct)
      )
    )

  val xml: Elem =
    <item>
      <label xml:lang="en">{layer.name}</label>
      {
      for (groupXml <- groups.filter(_.xml.isDefined).map(_.xml.get))
        yield groupXml
    }
  </item>
}
