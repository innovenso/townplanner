package com.innovenso.townplanner.io.openexchange.model

import com.innovenso.townplanner.model.concepts.{
  Actor,
  ArchitectureBuildingBlock,
  BusinessActor,
  BusinessCapability,
  Decision,
  DecisionOption,
  ItContainer,
  ItPlatform,
  ItProject,
  ItProjectMilestone,
  ItSystem,
  ItSystemIntegration,
  Organisation,
  Person,
  Principle,
  Team,
  Technology
}
import com.innovenso.townplanner.model.language.Element

import scala.xml.{Elem, NodeBuffer}

case class ElementOpenExchangeMapping(element: Element) {
  val openExchangeType: Option[String] = element match {
    case cap: BusinessCapability          => Some("Capability")
    case abb: ArchitectureBuildingBlock   => Some("ApplicationFunction")
    case platform: ItPlatform             => Some("ApplicationComponent")
    case system: ItSystem                 => Some("ApplicationComponent")
    case container: ItContainer           => Some("ApplicationComponent")
    case integration: ItSystemIntegration => Some("ApplicationInteraction")
    case actor: Actor                     => Some("BusinessActor")
    case actor: Person                    => Some("BusinessActor")
    case actor: Team                      => Some("BusinessActor")
    case actor: Organisation              => Some("BusinessActor")
    case project: ItProject               => Some("WorkPackage")
    case milestone: ItProjectMilestone    => Some("Plateau")
    case principle: Principle             => Some("Principle")
    case decision: Decision               => Some("Outcome")
    case decisionOption: DecisionOption   => Some("Assessment")
    case technology: Technology           => Some("Material")
    case _                                => None
  }
  val xml: Option[Elem] = openExchangeType.map(t =>
    <element identifier={element.key.camelCased} xsi:type={t}>
      <name xml:lang="en">
        {element.title}
      </name>
      {for (doc <- DocumentationOpenExchangeMapping(element).xml) yield doc}
    </element>
  )
}
