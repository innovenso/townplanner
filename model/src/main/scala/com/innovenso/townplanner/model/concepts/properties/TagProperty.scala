package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.concepts.Tag
import com.innovenso.townplanner.model.meta.{Key, SortKey}

case class TagProperty(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    tagKey: Key
) extends Property {
  val canBePlural: Boolean = true
}

object TagProperty {
  def apply(tag: Tag): TagProperty = new TagProperty(tagKey = tag.key)
}

trait HasTagProperties extends HasProperties {
  def tags: List[TagProperty] = props(classOf[TagProperty])
  def hasTag(tag: Tag): Boolean = tags.exists(tp => tp.tagKey == tag.key)
  def hasTag(key: Key): Boolean = tags.exists(tp => tp.tagKey == key)
  def withTag(tag: Tag): HasProperties = withProperty(TagProperty(tag))
  def withTag(tagProperty: TagProperty): HasProperties = withProperty(
    tagProperty
  )
  def withTag(key: Key): HasProperties = withProperty(TagProperty(tagKey = key))
}

trait CanConfigureTagProperties[ModelComponentType <: HasTagProperties] {
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def has(tag: Tag): HasTagProperties =
    propertyAdder.withProperty(modelComponent, TagProperty(tag))
  def isTagged(tag: Tag): HasTagProperties =
    propertyAdder.withProperty(modelComponent, TagProperty(tag))
}
