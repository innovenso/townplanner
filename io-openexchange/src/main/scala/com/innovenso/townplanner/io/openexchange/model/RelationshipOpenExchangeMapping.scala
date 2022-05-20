package com.innovenso.townplanner.io.openexchange.model

import com.innovenso.townplanner.model.concepts.relationships.{
  Association,
  Composition,
  Delivery,
  Flow,
  Impact,
  Implementation,
  Influence,
  Knowledge,
  RACI,
  Realization,
  Relationship,
  Serving,
  Stakeholder,
  Trigger
}

import scala.xml.Elem

case class RelationshipOpenExchangeMapping(relationship: Relationship) {
  val openExchangeType: Option[String] = relationship match {
    case composition: Composition       => Some("Composition")
    case realization: Realization       => Some("Realization")
    case association: Association       => Some("Association")
    case serving: Serving               => Some("Serving")
    case flow: Flow                     => Some("Flow")
    case delivery: Delivery             => Some("Assignment")
    case impact: Impact                 => Some("Association")
    case implementation: Implementation => Some("Association")
    case influence: Influence           => Some("Influence")
    case knowledge: Knowledge           => Some("Association")
    case raci: RACI                     => Some("Association")
    case stakeholder: Stakeholder       => Some("Association")
    case trigger: Trigger               => Some("Triggering")
    case _                              => None
  }
  val xml: Option[Elem] = openExchangeType.map(t =>
    <relationship identifier={relationship.key.camelCased} xsi:type={
      t
    } source={relationship.source.camelCased} target={
      relationship.target.camelCased
    }>
      <name xml:lang="en">
        {relationship.title}
      </name>
      {
      for (doc <- DocumentationOpenExchangeMapping(relationship).xml) yield doc
    }
    </relationship>
  )

}
