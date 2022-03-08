package com.innovenso.townplanner.model.language

import com.innovenso.townplanner.model.concepts.properties.HasFatherTime
import com.innovenso.townplanner.model.concepts.relationships.HasRelationships
import com.innovenso.townplanner.model.meta.{ADay, Key, Layer, Today}

trait View extends Concept {
  def pointInTime: ADay
  def layer: Layer
}

trait TimelessView extends View {
  override val pointInTime: ADay = Today
}

trait CompiledView[ViewType <: View]
    extends HasModelComponents
    with HasRelationships {
  def view: ViewType
  def pointInTime: ADay = view.pointInTime
  def layer: Layer = view.layer
  def title: String
  def key: Key = view.key
  def groupTitle: String
}

trait ViewCompiler[ViewType <: View, CompiledViewType <: CompiledView[
  ViewType
], ModelComponentSourceType <: HasModelComponents] {

  def view: ViewType
  def source: ModelComponentSourceType

  def compile: CompiledViewType

  def groupTitle(forConceptKey: Key): String = source
    .component(forConceptKey, classOf[Concept])
    .map(_.title)
    .getOrElse("Unknown Concept")
  def viewTitle: String = view.title

  def viewComponents(
      componentList: Iterable[_ <: ModelComponent]
  ): Map[Key, ModelComponent] = componentList
    .filter(visible)
    .map(c => c.key -> c)
    .toMap

  def visible(key: Key): Boolean =
    source.component(key, classOf[ModelComponent]).exists(visible)

  def visible(modelComponent: ModelComponent): Boolean = if (
    view.isInstanceOf[TimelessView]
  ) true
  else
    modelComponent match {
      case tm: HasFatherTime =>
        tm.isActive(view.pointInTime) || tm.isPhasingOut(view.pointInTime) || tm
          .isUnknownLifecycle(view.pointInTime)
      case _ => true
    }

}

trait HasViews extends HasModelComponents with HasRelationships {}
