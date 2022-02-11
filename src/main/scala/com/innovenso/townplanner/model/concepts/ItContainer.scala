package com.innovenso.townplanner.model.concepts

import com.innovenso.townplanner.model.concepts.properties.{
  CanAddProperties,
  CanConfigureArchitectureVerdict,
  CanConfigureCriticality,
  CanConfigureDescription,
  CanConfigureExternalIds,
  CanConfigureLinks,
  CanConfigureResilienceMeasures,
  HasArchitectureVerdict,
  HasCriticality,
  HasDescription,
  HasExternalIds,
  HasLinks,
  HasResilienceMeasures,
  Property
}
import com.innovenso.townplanner.model.concepts.relationships._
import com.innovenso.townplanner.model.language.{Element, HasModelComponents}
import com.innovenso.townplanner.model.meta._

trait ItContainer
    extends Element
    with HasDescription
    with HasArchitectureVerdict
    with HasCriticality
    with HasLinks
    with HasExternalIds
    with HasResilienceMeasures
    with CanBeFlowSource
    with CanBeFlowTarget
    with CanTrigger
    with CanBeTriggered
    with CanBeAssociated
    with CanCompose
    with CanBeImplemented
    with CanBeDelivered
    with CanBeImpacted
    with CanBeImplementedByTechnologies {
  val layer: Layer = ApplicationLayer
  val aspect: Aspect = ActiveStructure
  val modelComponentType: ModelComponentType = ModelComponentType(
    "IT Container"
  )
  def containerType: String
}

case class Microservice(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends ItContainer {
  val containerType: String = "microservice"
  def withProperty(property: Property): Microservice =
    copy(properties = this.properties + (property.key -> property))
}

case class Database(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends ItContainer {
  val containerType: String = "database"
  def withProperty(property: Property): Database =
    copy(properties = this.properties + (property.key -> property))
}

case class Service(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends ItContainer {
  val containerType: String = "service"
  def withProperty(property: Property): Service =
    copy(properties = this.properties + (property.key -> property))
}

case class Function(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends ItContainer {
  val containerType: String = "function"
  def withProperty(property: Property): Function =
    copy(properties = this.properties + (property.key -> property))
}

case class Filesystem(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends ItContainer {
  val containerType: String = "file system"
  def withProperty(property: Property): Filesystem =
    copy(properties = this.properties + (property.key -> property))
}

case class Queue(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends ItContainer {
  val containerType: String = "queue"
  def withProperty(property: Property): Queue =
    copy(properties = this.properties + (property.key -> property))
}

case class Topic(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends ItContainer {
  val containerType: String = "topic"
  def withProperty(property: Property): Topic =
    copy(properties = this.properties + (property.key -> property))
}

case class EventStream(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends ItContainer {
  val containerType: String = "event stream"
  def withProperty(property: Property): EventStream =
    copy(properties = this.properties + (property.key -> property))
}

case class Gateway(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends ItContainer {
  val containerType: String = "gateway"
  def withProperty(property: Property): Gateway =
    copy(properties = this.properties + (property.key -> property))
}

case class Proxy(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends ItContainer {
  val containerType: String = "proxy"
  def withProperty(property: Property): Proxy =
    copy(properties = this.properties + (property.key -> property))
}

case class Firewall(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends ItContainer {
  val containerType: String = "firewall"
  def withProperty(property: Property): Firewall =
    copy(properties = this.properties + (property.key -> property))
}

case class Cache(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends ItContainer {
  val containerType: String = "cache"
  def withProperty(property: Property): Cache =
    copy(properties = this.properties + (property.key -> property))
}

case class WebUI(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends ItContainer {
  val containerType: String = "web UI"
  def withProperty(property: Property): WebUI =
    copy(properties = this.properties + (property.key -> property))
}

case class MobileUI(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends ItContainer {
  val containerType: String = "mobile UI"
  def withProperty(property: Property): MobileUI =
    copy(properties = this.properties + (property.key -> property))
}

case class WatchUI(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends ItContainer {
  val containerType: String = "watch UI"
  def withProperty(property: Property): WatchUI =
    copy(properties = this.properties + (property.key -> property))
}

case class DesktopUI(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends ItContainer {
  val containerType: String = "desktop UI"
  def withProperty(property: Property): DesktopUI =
    copy(properties = this.properties + (property.key -> property))
}

case class TerminalUI(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends ItContainer {
  val containerType: String = "terminal UI"
  def withProperty(property: Property): TerminalUI =
    copy(properties = this.properties + (property.key -> property))
}

case class SmartTVUI(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends ItContainer {
  val containerType: String = "smart TV UI"
  def withProperty(property: Property): SmartTVUI =
    copy(properties = this.properties + (property.key -> property))
}

case class Batch(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends ItContainer {
  val containerType: String = "batch"
  def withProperty(property: Property): Batch =
    copy(properties = this.properties + (property.key -> property))
}

case class GenericContainer(
    key: Key = Key(),
    sortKey: SortKey = SortKey.next,
    title: String,
    containerType: String = "container",
    properties: Map[Key, Property] = Map.empty[Key, Property]
) extends ItContainer {
  def withProperty(property: Property): GenericContainer =
    copy(properties = this.properties + (property.key -> property))
}

trait HasItContainers extends HasModelComponents with HasRelationships {
  def containers: List[ItContainer] = components(classOf[ItContainer])
  def container(key: Key): Option[ItContainer] =
    component(key, classOf[ItContainer])
  def containers(itSystem: ItSystem): List[ItContainer] =
    directOutgoingDependencies(
      itSystem,
      classOf[Composition],
      classOf[ItContainer]
    )
  def system(itContainer: ItContainer): Option[ItSystem] =
    directIncomingDependencies(
      itContainer,
      classOf[Composition],
      classOf[ItSystem]
    ).headOption

}

case class ItContainerConfigurer[ContainerType <: ItContainer](
    modelComponent: ContainerType,
    propertyAdder: CanAddProperties,
    relationshipAdder: CanAddRelationships
) extends CanConfigureDescription[ContainerType]
    with CanConfigureArchitectureVerdict[ContainerType]
    with CanConfigureLinks[ContainerType]
    with CanConfigureExternalIds[ContainerType]
    with CanConfigureCriticality[ContainerType]
    with CanConfigureResilienceMeasures[ContainerType]
    with CanConfigureImplementationTarget[ContainerType]
    with CanConfigureTriggerSource[ContainerType]
    with CanConfigureTriggerTarget[ContainerType]
    with CanConfigureFlowSource[ContainerType]
    with CanConfigureFlowTarget[ContainerType]
    with CanConfigureAssociations[ContainerType]
    with CanConfigureCompositionTarget[ContainerType]
    with CanConfigureDeliveryTarget[ContainerType] {
  def as(
      body: ItContainerConfigurer[ContainerType] => Any
  ): ContainerType = {
    body.apply(this)
    propertyAdder.townPlan
      .component(modelComponent.key, modelComponent.getClass)
      .get
  }
}

trait CanAddItContainers extends CanAddProperties with CanAddRelationships {
  def describes(
      container: GenericContainer
  ): ItContainerConfigurer[GenericContainer] =
    describesContainer[GenericContainer](container)

  def describes(
      container: Microservice
  ): ItContainerConfigurer[Microservice] =
    describesContainer[Microservice](container)

  def describes(
      container: Database
  ): ItContainerConfigurer[Database] =
    describesContainer[Database](container)

  def describes(
      container: Queue
  ): ItContainerConfigurer[Queue] =
    describesContainer[Queue](container)

  def describes(
      container: Topic
  ): ItContainerConfigurer[Topic] =
    describesContainer[Topic](container)

  def describes(
      container: EventStream
  ): ItContainerConfigurer[EventStream] =
    describesContainer[EventStream](container)

  def describes(
      container: Filesystem
  ): ItContainerConfigurer[Filesystem] =
    describesContainer[Filesystem](container)

  def describes(
      container: Service
  ): ItContainerConfigurer[Service] =
    describesContainer[Service](container)

  def describes(
      container: Function
  ): ItContainerConfigurer[Function] =
    describesContainer[Function](container)

  def describes(
      container: Gateway
  ): ItContainerConfigurer[Gateway] =
    describesContainer[Gateway](container)

  def describes(
      container: Proxy
  ): ItContainerConfigurer[Proxy] =
    describesContainer[Proxy](container)

  def describes(
      container: Firewall
  ): ItContainerConfigurer[Firewall] =
    describesContainer[Firewall](container)

  def describes(
      container: WebUI
  ): ItContainerConfigurer[WebUI] =
    describesContainer[WebUI](container)

  def describes(
      container: MobileUI
  ): ItContainerConfigurer[MobileUI] =
    describesContainer[MobileUI](container)

  def describes(
      container: WatchUI
  ): ItContainerConfigurer[WatchUI] =
    describesContainer[WatchUI](container)

  def describes(
      container: DesktopUI
  ): ItContainerConfigurer[DesktopUI] =
    describesContainer[DesktopUI](container)

  def describes(
      container: SmartTVUI
  ): ItContainerConfigurer[SmartTVUI] =
    describesContainer[SmartTVUI](container)

  def describes(
      container: TerminalUI
  ): ItContainerConfigurer[TerminalUI] =
    describesContainer[TerminalUI](container)

  def describes(
      container: Batch
  ): ItContainerConfigurer[Batch] =
    describesContainer[Batch](container)

  def describes(
      container: Cache
  ): ItContainerConfigurer[Cache] =
    describesContainer[Cache](container)

  private def describesContainer[ContainerType <: ItContainer](
      container: ContainerType
  ): ItContainerConfigurer[ContainerType] =
    ItContainerConfigurer(has(container), this, this)
}
