package com.innovenso.townplanner.io.model

import com.innovenso.townplan.io.context.{Html, Output}
import com.innovenso.townplanner.model.language.Element

import java.nio.file.{Path, Paths}

case class NavigationContext (element: Element) {
  val path: Path = Paths.get(element.layer.name + "/" + element.modelComponentType.value)
  val assetName: String = "website/" + path.toString + "/" + element.title + Html.extension
  def relativePathTo(otherElement: Element): String = path.relativize(NavigationContext(otherElement).path).toString
  def relativePathToStaticAsset(output: Output): String = path.relativize(Paths.get("static/" + output.assetHashedName)).toString
}
