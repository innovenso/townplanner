package com.innovenso.townplanner.io

import com.innovenso.townplanner.io.context.{HtmlLibrary, HtmlLibraryFile}

case object TestWebsiteTheme extends HtmlLibrary {
  val name = "testWebsiteTheme"
  val resourceBasePathName: Option[String] = Some(
    "test/theme"
  )
  val files: List[HtmlLibraryFile] = List(
    "logo.svg"
  ).map(name => HtmlLibraryFile(targetFileName = name, resourcePath = name))

}
