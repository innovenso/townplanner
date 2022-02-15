package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{CanAddProperties, CanAddRequirementScores, CanConfigureContext, CanConfigureDataProtectionConcerns, CanConfigureDescription, CanConfigureFatherTime, CanConfigureLinks, CanConfigureRequirements, CanConfigureSecurityImpact, HasContext, HasDataProtectionConcerns, HasDescription, HasFatherTime, HasLinks, HasRequirementScores, HasRequirements, HasSecurityImpact, Property}
import com.innovenso.townplanner.model.concepts.relationships.{CanAddRelationships, CanBeAssociated, CanBeComposedOf, CanBeInfluenced, CanBeTriggered, CanCompose, CanConfigureAssociations, CanConfigureCompositionSource, CanConfigureCompositionTarget, CanConfigureImpactSource, CanConfigureInfluenceTarget, CanConfigureRaciTarget, CanConfigureServingSource, CanConfigureStakeHolderTarget, CanConfigureTriggerTarget, CanHaveRaci, CanHaveStakeholder, CanImpact, CanServe, Composition, HasRelationships, Serving}
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta.{ActiveStructure, Aspect, ImplementationLayer, Key, Layer, ModelComponentType, SortKey}

case class ItProject(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Element
    with HasDescription
    with HasLinks
    with HasFatherTime
    with HasDataProtectionConcerns
    with HasSecurityImpact
    with CanImpact
    with CanServe
    with CanBeAssociated
    with CanBeInfluenced
    with CanBeComposedOf
    with CanHaveRaci
    with CanHaveStakeholder
    with CanBeTriggered
    with HasRequirements
    with HasContext {
  val layer: Layer = ImplementationLayer
  val aspect: Aspect = ActiveStructure
  val modelComponentType: ModelComponentType = ModelComponentType("IT Project")

  def withProperty(property: Property): ItProject =
    copy(properties = this.properties + (property.key -> property))

}

case class ItProjectMilestone(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends Element
    with HasDescription
    with HasLinks
    with HasFatherTime
    with HasDataProtectionConcerns
    with HasSecurityImpact
    with CanImpact
    with CanServe
    with CanBeAssociated
    with CanBeInfluenced
    with CanCompose
    with CanHaveRaci
    with CanHaveStakeholder
    with CanBeTriggered
    with HasRequirements
    with HasContext {
  val layer: Layer = ImplementationLayer
  val aspect: Aspect = ActiveStructure
  val modelComponentType: ModelComponentType = ModelComponentType(
    "IT Project Milestone"
  )

  def withProperty(property: Property): ItProjectMilestone =
    copy(properties = this.properties + (property.key -> property))

}

trait HasProjects extends HasModelComponents with HasRelationships {
  def itProjects(enterprise: Enterprise): List[ItProject] = directIncomingDependencies(
    enterprise,
    classOf[Serving],
    classOf[ItProject])
  def itProjects: List[ItProject] = components(classOf[ItProject])
  def itProject(key: Key): Option[ItProject] =
    component(key, classOf[ItProject])
  def itProjectMilestone(key: Key): Option[ItProjectMilestone] =
    component(key, classOf[ItProjectMilestone])
  def itProjectMilestones(itProject: ItProject): List[ItProjectMilestone] =
    directOutgoingDependencies(
      itProject,
      classOf[Composition],
      classOf[ItProjectMilestone]
    )
}

case class ItProjectConfigurer(
    modelComponent: ItProject,
    propertyAdder: CanAddProperties,
    relationshipAdder: CanAddRelationships
) extends CanConfigureDescription[ItProject]
    with CanConfigureLinks[ItProject]
    with CanConfigureDataProtectionConcerns[ItProject]
    with CanConfigureSecurityImpact[ItProject]
    with CanConfigureCompositionSource[ItProject]
    with CanConfigureServingSource[ItProject]
    with CanConfigureImpactSource[ItProject]
    with CanConfigureFatherTime[ItProject]
    with CanConfigureStakeHolderTarget[ItProject]
    with CanConfigureRaciTarget[ItProject]
    with CanConfigureInfluenceTarget[ItProject]
    with CanConfigureTriggerTarget[ItProject]
    with CanConfigureAssociations[ItProject]
    with CanConfigureRequirements[ItProject]
    with CanConfigureContext[ItProject] {
  def as(
      body: ItProjectConfigurer => Any
  ): ItProject = {
    body.apply(this)
    propertyAdder.townPlan
      .itProject(modelComponent.key)
      .get
  }
}

case class ItProjectMilestoneConfigurer(
    modelComponent: ItProjectMilestone,
    propertyAdder: CanAddProperties,
    relationshipAdder: CanAddRelationships
) extends CanConfigureDescription[ItProjectMilestone]
    with CanConfigureLinks[ItProjectMilestone]
    with CanConfigureDataProtectionConcerns[ItProjectMilestone]
    with CanConfigureSecurityImpact[ItProjectMilestone]
    with CanConfigureCompositionTarget[ItProjectMilestone]
    with CanConfigureServingSource[ItProjectMilestone]
    with CanConfigureImpactSource[ItProjectMilestone]
    with CanConfigureFatherTime[ItProjectMilestone]
    with CanConfigureStakeHolderTarget[ItProjectMilestone]
    with CanConfigureRaciTarget[ItProjectMilestone]
    with CanConfigureInfluenceTarget[ItProjectMilestone]
    with CanConfigureTriggerTarget[ItProjectMilestone]
    with CanConfigureAssociations[ItProjectMilestone]
    with CanConfigureRequirements[ItProjectMilestone]
    with CanConfigureContext[ItProjectMilestone] {
  def as(
      body: ItProjectMilestoneConfigurer => Any
  ): ItProjectMilestone = {
    body.apply(this)
    propertyAdder.townPlan
      .itProjectMilestone(modelComponent.key)
      .get
  }
}

trait CanAddProjects extends CanAddProperties with CanAddRelationships {
  def describes(project: ItProject): ItProjectConfigurer =
    ItProjectConfigurer(has(project), this, this)
  def describes(milestone: ItProjectMilestone): ItProjectMilestoneConfigurer =
    ItProjectMilestoneConfigurer(has(milestone), this, this)
}
