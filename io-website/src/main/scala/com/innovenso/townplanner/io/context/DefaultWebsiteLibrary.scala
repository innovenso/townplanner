package com.innovenso.townplanner.io.context

case object DefaultWebsiteLibrary extends HtmlLibrary {
  val name = "innovensoWebsite"
  val resourceBasePathName: Option[String] = Some(
    "html/static/innovenso"
  )
  val files: List[HtmlLibraryFile] = List(
    "d3.v4.min.js",
    "pico.min.css",
    "radar-0.6.js",
    "townplanner.css"
  ).map(name => HtmlLibraryFile(targetFileName = name, resourcePath = name))

}
