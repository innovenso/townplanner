package com.innovenso.townplanner.io.openexchange.model

import com.innovenso.townplanner.model.language.Element

import scala.xml.Elem

case class ModelComponentTypeOrganisationOpenExchangeMapping(
    modelComponentType: String,
    elements: List[Element]
) {
  val xml: Option[Elem] =
    if (elements.isEmpty) None
    else
      Some(
        <item>
      <label xml:lang="en">{modelComponentType}</label>
      {
          for (element <- elements) yield <item identifierRef={
            element.key.camelCased
          }/>
        }
    </item>
      )
}
