package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{CanAddProperties, CanConfigureArchitectureVerdict, CanConfigureCriticality, CanConfigureDescription, CanConfigureExternalIds, CanConfigureFatherTime, CanConfigureLinks, CanConfigureResilienceMeasures, CanConfigureSWOT, HasDescription, HasTagProperties, Property}
import com.innovenso.townplanner.model.concepts.relationships.{CanAddRelationships, CanConfigureAssociations, CanConfigureCompositionSource, CanConfigureFlowSource, CanConfigureFlowTarget, CanConfigureImplementationTarget, CanConfigureKnowledgeTarget, CanConfigureRealizationSource, CanConfigureTriggerSource, CanConfigureTriggerTarget}
import com.innovenso.townplanner.model.language.{Concept, HasModelComponents, ModelComponent}
import com.innovenso.townplanner.model.meta.{Aspect, Color, Key, Layer, ModelComponentType, NoStructure, OtherLayer, PassiveStructure, SortKey, TechnologyLayer}

case class Tag(key: Key = Key(), sortKey: SortKey = SortKey.next,
               title: String,
               color: Color = Color.random,
               properties: Map[Key, Property] = Map.empty[Key, Property]) extends Concept with HasDescription {
  val modelComponentType: ModelComponentType = ModelComponentType("Tag")
  val aspect: Aspect = NoStructure
  val layer: Layer = OtherLayer

  def withProperty(property: Property): Tag =
    copy(properties = this.properties + (property.key -> property))
}

trait HasTags extends HasModelComponents {
  def tags: List[Tag] = components(classOf[Tag])
  def tag(key: Key): Option[Tag] =
    component(key, classOf[Tag])
  def taggedComponents[ComponentClass <: ModelComponent with HasTagProperties](tag: Tag, shouldBeOfClass: Class[ComponentClass]): List[ComponentClass] = components(shouldBeOfClass).filter(c => c.tags.exists(tp => tp.tagKey == tag.key))
  def tags(component: HasTagProperties): List[Tag] = component.tags.flatMap(tp => tag(tp.tagKey))
}

case class TagConfigurer(
                                 modelComponent: Tag,
                                 propertyAdder: CanAddProperties
                               ) extends CanConfigureDescription[Tag] {
  def as(
          body: TagConfigurer => Any
        ): Tag = {
    body.apply(this)
    propertyAdder.townPlan
      .tag(modelComponent.key)
      .get
  }
}

trait CanAddTags extends CanAddProperties {
  def describes(tag: Tag): TagConfigurer =
    TagConfigurer(has(tag), this)
}

