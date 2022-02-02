package com.innovenso.townplanner.model.language

import com.innovenso.townplanner.model.concepts.{HasRelationships, Relationship}
import com.innovenso.townplanner.model.meta.{Aspect, Layer}

trait Element extends Concept {
  def aspect: Aspect
  def layer: Layer
}
