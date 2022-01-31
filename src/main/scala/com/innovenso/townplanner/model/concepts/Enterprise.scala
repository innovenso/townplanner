package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.language.Element
import com.innovenso.townplanner.model.meta.{ActiveStructure, Aspect, Business, ConceptType, Description, Key, Layer, PassiveStructure, SortKey, Title}

case class Enterprise(key: Key, sortKey: SortKey, title: Title, description: Description) extends Element {
  val layer: Layer = Business
  val aspect: Aspect = ActiveStructure
  val conceptType: ConceptType = ConceptType("Enterprise")
}

