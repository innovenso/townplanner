package com.innovenso.townplanner.io.model

import com.innovenso.townplan.io.context.{Html, Output}
import com.innovenso.townplanner.model.language.Element

import java.nio.file.{Path, Paths}

case class NavigationContext (element: Element) {
  private def flatten(value: String): String = value.replaceAll("[^a-zA-Z0-9]","_").toLowerCase
  private val layer: String = flatten(element.layer.name)
  private val modelComponentType: String = flatten(element.modelComponentType.value)
  private val filename: String = flatten(element.title) + Html.extension
  private val websiteRootPath: Path = Paths.get("website")

  val path: Path = Paths.get(layer, modelComponentType)
  val fullPath: Path = path.resolve(filename)
  val assetPath: Path = websiteRootPath.resolve(fullPath)
  val assetName: String = assetPath.toString
  def relativePathTo(otherElement: Element): String = path.relativize(NavigationContext(otherElement).fullPath).toString
  def relativePathToStaticAsset(output: Output): String = path.relativize(Paths.get("static", output.assetHashedName)).toString
}
