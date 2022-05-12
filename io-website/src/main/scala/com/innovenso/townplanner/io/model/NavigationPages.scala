package com.innovenso.townplanner.io.model

import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.language.Element
import com.innovenso.townplanner.model.meta.{Layer, ModelComponentType}

import java.nio.file.{Path, Paths}

case class NavigationPages (townPlan: TownPlan) {
  val layers: List[Layer] = Layer.values
  val elements: List[Element] = townPlan.components(classOf[Element])
  def modelComponentTypes(layer: Layer): List[ModelComponentType] = elements.filter(element => element.layer == layer).map(_.modelComponentType).distinct.sortBy(_.value)
  def elementsOfType(modelComponentType: ModelComponentType): List[Element] = elements.filter(element => element.modelComponentType == modelComponentType).sortBy(_.title)

  val home: IndexPage = IndexPage("Town Plan", Paths.get(""), layers.map(layer => MenuItem(NavigationContext.layerPathFromHome(layer).toString, layer.name)))

  val layerIndexes: List[IndexPage] = layers.map(layer => IndexPage(layer.name, NavigationContext.layerPath(layer), modelComponentTypes(layer).map(mct => MenuItem(NavigationContext.modelComponentTypeIndexPathFromLayerIndex(mct).toString, mct.value))))

  val modelComponentIndexes: List[IndexPage] = layers.flatMap(layer => modelComponentTypes(layer).map(mct => IndexPage(mct.value, NavigationContext.modelComponentTypePath(layer, mct), elementsOfType(mct).map(element => MenuItem(NavigationContext.elementPathFromModelComponentTypeIndex(element).toString, element.title)))))

  val pages: List[IndexPage] = List(home) ::: layerIndexes ::: modelComponentIndexes
}

case class IndexPage(title: String, path: Path, items: List[MenuItem]) extends CanNavigate {
  override def filename: String = "index.html"
}
