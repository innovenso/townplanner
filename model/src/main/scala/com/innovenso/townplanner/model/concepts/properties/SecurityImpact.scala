package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.meta.{Key, SortKey}

abstract class SecurityImpact extends Property {
  val key: Key = Key()
  val sortKey: SortKey = SortKey.next
  val canBePlural: Boolean = false
  def name: String
  def level: SecurityImpactLevel
  def description: String
}

sealed trait SecurityImpactLevel {
  def name: String
  def character: Char
}

case object LowImpact extends SecurityImpactLevel {
  val name = "Low"
  val character = 'L'
}

case object MediumImpact extends SecurityImpactLevel {
  val name = "Medium"
  val character = 'M'
}

case object HighImpact extends SecurityImpactLevel {
  val name = "High"
  val character = 'H'
}

case object ExtremelyHighImpact extends SecurityImpactLevel {
  val name = "Extremely High"
  val character = 'E'
}

case object UnknownImpact extends SecurityImpactLevel {
  val name = "Unknown"
  val character = 'X'
}

case class Confidentiality(
    level: SecurityImpactLevel = UnknownImpact,
    description: String = ""
) extends SecurityImpact {
  val name = "Confidentiality"
}

case class Integrity(
    level: SecurityImpactLevel = UnknownImpact,
    description: String = ""
) extends SecurityImpact {
  val name = "Integrity"
}

case class Availability(
    level: SecurityImpactLevel = UnknownImpact,
    description: String = ""
) extends SecurityImpact {
  val name = "Availability"
}

trait HasSecurityImpact extends HasProperties {
  def securityImpacts: List[SecurityImpact] = props(classOf[SecurityImpact])
  def confidentialityImpact: Option[Confidentiality] =
    props(classOf[Confidentiality]).headOption
  def integrityImpact: Option[Integrity] =
    props(classOf[Integrity]).headOption
  def availabilityImpact: Option[Availability] =
    props(classOf[Availability]).headOption
  def withSecurityImpact[SecurityImpactType <: SecurityImpact](
      impact: SecurityImpactType
  ): HasProperties =
    withProperty(impact)
}

case class SecurityImpactConfigurer(
    modelComponent: HasSecurityImpact,
    securityImpactLevel: SecurityImpactLevel,
    propertyAdder: CanAddProperties
) {
  def on(confidentiality: Confidentiality): HasSecurityImpact =
    propertyAdder.withProperty(
      modelComponent,
      confidentiality.copy(level = securityImpactLevel)
    )
  def on(integrity: Integrity): HasSecurityImpact =
    propertyAdder.withProperty(
      modelComponent,
      integrity.copy(level = securityImpactLevel)
    )
  def on(availability: Availability): HasSecurityImpact =
    propertyAdder.withProperty(
      modelComponent,
      availability.copy(level = securityImpactLevel)
    )

}

trait CanConfigureSecurityImpact[ModelComponentType <: HasSecurityImpact] {
  def propertyAdder: CanAddProperties
  def modelComponent: ModelComponentType

  def has(level: SecurityImpactLevel): SecurityImpactConfigurer =
    SecurityImpactConfigurer(modelComponent, level, propertyAdder)
}
