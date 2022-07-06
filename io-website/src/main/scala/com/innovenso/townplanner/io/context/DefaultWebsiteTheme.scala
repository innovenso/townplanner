package com.innovenso.townplanner.io.context

case object DefaultWebsiteTheme extends HtmlLibrary {
  val name = "innovensoWebsiteTheme"
  val resourceBasePathName: Option[String] = Some(
    "html/theme/innovenso"
  )
  val files: List[HtmlLibraryFile] = List(
    "logo.svg"
  ).map(name => HtmlLibraryFile(targetFileName = name, resourcePath = name))

}
