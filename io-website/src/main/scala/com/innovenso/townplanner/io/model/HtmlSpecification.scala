package com.innovenso.townplanner.io.model

import com.innovenso.townplan.io.context.{Html, MarkDown, OutputFileType}
import com.innovenso.townplanner.model.language.Element

case class HtmlSpecification(element: Element, renderFunction: NavigationContext => String) {
  val navigationContext: NavigationContext = NavigationContext(element)
  val assetName: String = navigationContext.assetName
  val html: String = renderFunction(navigationContext)
}
