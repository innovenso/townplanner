package com.innovenso.townplanner.io.model

import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.language.Element
import com.innovenso.townplanner.model.meta.{
  Layer,
  ModelComponentType,
  OtherLayer
}

case class NavigationTree(canNavigate: CanNavigate, townPlan: TownPlan) {
  val layers: List[Layer] = Layer.values
  val elements: List[Element] = townPlan.components(classOf[Element])
  val elementsMenu: List[MenuItem] = elements
    .filter(element => !List(OtherLayer).contains(element.layer))
    .filter(element =>
      !List("Decision Option", "IT Project Milestone").contains(
        element.modelComponentType.value
      )
    )
    .map(element =>
      MenuItem(
        canNavigate.relativePathTo(element.layer, element.modelComponentType),
        s"${element.layer.name} - ${element.modelComponentType.value}"
      )
    )
    .sortBy(_.title)
    .distinct
  val mainMenu: List[Menu] = layers
    .filter(layer => elements.exists(element => element.layer == layer))
    .map(layer => Menu(layer.name, layerMenu(layer)))
  def layerMenu(layer: Layer): List[MenuItem] = elements
    .filter(element => element.layer == layer)
    .map(_.modelComponentType)
    .distinct
    .sortBy(_.value)
    .map(modelComponentType =>
      MenuItem(
        canNavigate.relativePathTo(layer, modelComponentType),
        modelComponentType.value
      )
    )
  def modelComponentTypeMenu(
      modelComponentType: ModelComponentType
  ): List[MenuItem] = elements
    .filter(element => element.modelComponentType == modelComponentType)
    .sortBy(_.title)
    .map(element =>
      MenuItem(canNavigate.relativePathTo(element), element.title)
    )
}

case class Menu(name: String, items: List[MenuItem])

case class MenuItem(url: String, title: String)
