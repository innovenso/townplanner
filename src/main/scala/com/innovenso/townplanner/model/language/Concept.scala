package com.innovenso.townplanner.model.language

import com.innovenso.townplanner.model.meta.{Aspect, Description, Layer, Title}

trait Concept extends ModelComponent {
  def aspect: Aspect
  def layer: Layer
  def title: Title
  def description: Description
}
