package com.innovenso.townplanner.io.latex.document.model

import com.innovenso.townplanner.io.latex.model.{LatexLibrary, LatexLibraryFile}

case object LegrangeBookLibrary extends LatexLibrary {
  val name = "innovensoBook"
  val resourceBasePathName: Option[String] = Some(
    "latex/templates/book/innovenso"
  )
  val files: List[LatexLibraryFile] = List(
    "indexstyle.ist",
    "LegrandOrangeBook.cls",
    "empty.bib"
  ).map(name => LatexLibraryFile(resourcePath = name))

}
