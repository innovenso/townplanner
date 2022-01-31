package com.innovenso.townplanner.model.language

import com.innovenso.townplanner.model.meta.{Aspect, ConceptType, Description, Key, Layer, SortKey, Title}

trait Concept extends ModelComponent {
  def conceptType: ConceptType
  def aspect: Aspect
  def layer: Layer
  def title: Title
  def description: Description
}
