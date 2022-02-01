package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.language.ModelComponent
import com.innovenso.townplanner.model.meta.{Key, ModelComponentType, SortKey}

case class Relationship(key: Key, sortKey: SortKey) extends ModelComponent {
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Relationship"
  )
}
