package com.innovenso.townplanner.io.latex.document.model

import com.innovenso.townplanner.io.latex.model.{LatexLibrary, LatexLibraryFile}

case object BookDefaultThemeLibrary extends LatexLibrary {
  val name = "legrangetheme"
  val resourceBasePathName: Option[String] = Some("latex/themes/book/innovenso")
  val imageFiles: List[LatexLibraryFile] =
    List(
      "background.pdf",
      "chapter1.pdf",
      "chapter2.pdf",
      "chapter3.pdf"
    ).map(name =>
      LatexLibraryFile(
        resourcePath = name,
        targetFileName = Some(s"images/$name")
      )
    )
  val files: List[LatexLibraryFile] = imageFiles
}
