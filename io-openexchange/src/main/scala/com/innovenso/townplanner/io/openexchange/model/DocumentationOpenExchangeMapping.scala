package com.innovenso.townplanner.io.openexchange.model

import com.innovenso.townplanner.model.concepts.properties.HasDescription
import com.innovenso.townplanner.model.language.Element

import scala.xml.Elem

case class DocumentationOpenExchangeMapping(element: Element) {
  val xml: Seq[Elem] = element match {
    case hasDescription: HasDescription =>
      hasDescription.descriptions.map(description =>
        <documentation xml:lang="en">{description.value}</documentation>
      )
    case _ => Nil
  }
}
