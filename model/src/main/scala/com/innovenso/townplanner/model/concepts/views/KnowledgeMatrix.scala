package com.innovenso.townplanner.model.concepts.views

import com.innovenso.townplanner.model.concepts.{
  BusinessActor,
  BusinessCapability,
  Enterprise,
  HasBusinessActors,
  HasBusinessCapabilities,
  HasEnterprises,
  HasTags,
  HasTechnologies,
  LanguageOrFramework,
  Person,
  Platform,
  Tag,
  Team,
  Technique,
  Technology,
  Tool
}
import com.innovenso.townplanner.model.concepts.properties.{
  CanAddProperties,
  Property
}
import com.innovenso.townplanner.model.concepts.relationships.{
  CanAddRelationships,
  HasRelationships,
  Knowledge,
  KnowledgeLevel,
  NoKnowledge
}
import com.innovenso.townplanner.model.language.{
  CompiledView,
  HasViews,
  ModelComponent,
  TimelessView,
  ViewCompiler
}
import com.innovenso.townplanner.model.meta.{
  Key,
  Layer,
  ModelComponentType,
  MotivationLayer,
  SortKey,
  StrategyLayer
}

case class KnowledgeMatrix(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    forTeam: Key,
    title: String = "Knowledge Matrix",
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends TimelessView {
  val modelComponentType: ModelComponentType = ModelComponentType(
    "Knowledge Matrix"
  )
  val layer: Layer = StrategyLayer

  def withProperty(property: Property): KnowledgeMatrix =
    copy(properties = this.properties + (property.key -> property))
}

object KnowledgeMatrix {
  def apply(forTeam: Team): KnowledgeMatrix = new KnowledgeMatrix(
    forTeam = forTeam.key,
    title = s"${forTeam.title} Knowledge Matrix"
  )
}

trait HasKnowledgeMatrices
    extends HasViews
    with HasTechnologies
    with HasBusinessActors {
  def knowledgeMatrices: List[CompiledKnowledgeMatrix] = components(
    classOf[KnowledgeMatrix]
  ).map(view => KnowledgeMatrixCompiler(view, this).compile)
  def knowledgeMatrix(key: Key): Option[CompiledKnowledgeMatrix] =
    component(key, classOf[KnowledgeMatrix]).map(
      KnowledgeMatrixCompiler(_, this).compile
    )
}

trait CanAddKnowledgeMatrices
    extends CanAddProperties
    with CanAddRelationships {
  def needs(
      knowledgeMatrix: KnowledgeMatrix
  ): KnowledgeMatrix =
    has(knowledgeMatrix)
}

case class CompiledKnowledgeMatrix(
    view: KnowledgeMatrix,
    title: String,
    groupTitle: String,
    modelComponents: Map[Key, ModelComponent]
) extends CompiledView[KnowledgeMatrix]
    with HasRelationships
    with HasTechnologies
    with HasBusinessActors {
  val languagesOrFrameworks: List[LanguageOrFramework] = technologies(
    classOf[LanguageOrFramework]
  )
  val tools: List[Tool] = technologies(classOf[Tool])
  val techniques: List[Technique] = technologies(classOf[Technique])
  val platforms: List[Platform] = technologies(classOf[Platform])
  val team: Option[Team] = teamActors.headOption.map(_.asInstanceOf[Team])
  val members: List[Person] = individualActors.map(_.asInstanceOf[Person])
  def level(person: BusinessActor, tech: Technology): KnowledgeLevel =
    businessActor(person.key)
      .flatMap(a =>
        technology(tech.key).flatMap(t =>
          relationshipsBetween(a, t, classOf[Knowledge]).headOption.map(_.level)
        )
      )
      .getOrElse(NoKnowledge)
}

case class KnowledgeMatrixCompiler(
    view: KnowledgeMatrix,
    source: HasKnowledgeMatrices
) extends ViewCompiler[
      KnowledgeMatrix,
      CompiledKnowledgeMatrix,
      HasKnowledgeMatrices
    ] {
  def compile: CompiledKnowledgeMatrix =
    CompiledKnowledgeMatrix(
      view,
      viewTitle,
      groupTitle(view.forTeam),
      viewComponents(
        team.toList ::: people ::: technologies ::: knowledge
      )
    )

  private val technologies = source.technologies

  private val team: Option[BusinessActor] = source.businessActor(view.forTeam)

  private val people: List[BusinessActor] =
    team.map(t => source.teamMembers(t.key)).getOrElse(Nil)

  private val knowledge: List[Knowledge] =
    people.flatMap(p =>
      source
        .relationships(p, classOf[Knowledge], classOf[Technology])
        .map(_.asInstanceOf[Knowledge])
    )
}
