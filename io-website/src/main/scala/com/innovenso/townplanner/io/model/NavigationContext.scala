package com.innovenso.townplanner.io.model

import com.innovenso.townplan.io.context.{Html, Output}
import com.innovenso.townplanner.model.language.Element
import com.innovenso.townplanner.model.meta.{Layer, ModelComponentType}

import java.nio.file.{Path, Paths}

case class NavigationContext (element: Element) extends CanNavigate {
  private val layer: String = NavigationContext.flatten(element.layer.name)
  private val modelComponentType: String = NavigationContext.flatten(element.modelComponentType.value)
  val filename: String = NavigationContext.flatten(element.title) + Html.extension

  val path: Path = Paths.get(layer, modelComponentType)
}

trait CanNavigate {
  def filename: String
  def path: Path
  def fullPath: Path = path.resolve(filename)
  def assetPath: Path = NavigationContext.websiteRootPath.resolve(fullPath)
  def assetName: String = assetPath.toString
  def relativePathTo(otherElement: Element): String = path.relativize(NavigationContext(otherElement).fullPath).toString
  def relativePathToStaticAsset(output: Output): String = path.relativize(Paths.get("static", output.assetHashedName)).toString
  def relativePathToStaticAsset(filename: String): String = path.relativize(Paths.get("static", filename)).toString
  def relativePathTo(targetLayer: Layer, targetModelComponentType: ModelComponentType): String = path.relativize(NavigationContext.modelComponentTypeIndexPath(targetLayer, targetModelComponentType)).toString
  def home: String = path.relativize(Paths.get("index.html")).toString
}

object NavigationContext {
  val websiteRootPath: Path = Paths.get("website")

  def flatten(value: String): String = value.replaceAll("[^a-zA-Z0-9]","_").toLowerCase
  def layerPath(layer: Layer): Path = Paths.get(flatten(layer.name))
  def modelComponentTypePath(layer: Layer, modelComponentType: ModelComponentType): Path = Paths.get(flatten(layer.name), flatten(modelComponentType.value))
  def modelComponentTypeIndexPath(layer: Layer, modelComponentType: ModelComponentType): Path = modelComponentTypePath(layer, modelComponentType).resolve("index.html")
  def elementPathFromModelComponentTypeIndex(element: Element): Path = Paths.get(flatten(element.title) + Html.extension)
  def modelComponentTypeIndexPathFromLayerIndex(modelComponentType: ModelComponentType): Path = Paths.get(flatten(modelComponentType.value), "index.html")
  def layerPathFromHome(layer: Layer): Path = Paths.get(flatten(layer.name), "index.html")
}