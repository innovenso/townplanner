package com.innovenso.townplanner.model.language

import com.innovenso.townplanner.model.concepts.properties.HasFatherTime
import com.innovenso.townplanner.model.concepts.relationships.HasRelationships
import com.innovenso.townplanner.model.meta.{ADay, Key}

trait View extends Concept {
  def pointInTime: ADay
}

trait CompiledView[ViewType <: View]
    extends HasModelComponents
    with HasRelationships {
  def view: ViewType
}

trait ViewCompiler[ViewType <: View, CompiledViewType <: CompiledView[
  ViewType
], ModelComponentSourceType <: HasModelComponents] {

  def view: ViewType
  def source: ModelComponentSourceType

  def compile: CompiledViewType

  def viewComponents(
      componentList: Iterable[_ <: ModelComponent]
  ): Map[Key, ModelComponent] = componentList
    .filter(visible)
    .map(c => c.key -> c)
    .toMap

  def visible(modelComponent: ModelComponent): Boolean =
    modelComponent match {
      case tm: HasFatherTime =>
        tm.isActive(view.pointInTime) || tm.isPhasingOut(view.pointInTime) || tm
          .isUnknownLifecycle(view.pointInTime)
      case _ => true
    }

  def visible(key: Key): Boolean =
    source.component(key, classOf[ModelComponent]).exists(visible)

}
