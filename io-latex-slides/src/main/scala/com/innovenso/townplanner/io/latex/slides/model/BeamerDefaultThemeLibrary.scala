package com.innovenso.townplanner.io.latex.slides.model

import com.innovenso.townplanner.io.latex.model.{LatexLibrary, LatexLibraryFile}

case object BeamerDefaultThemeLibrary extends LatexLibrary {
  val name = "beamerDefaultTheme"
  val resourceBasePathName: Option[String] = Some("beamer/theme/default")
  val styleFiles: List[LatexLibraryFile] = List(
    "beamercolorthemeinnovenso.sty",
    "beamerfontthemeinnovenso.sty",
    "beamerinnerthemeinnovenso.sty",
    "beamerouterthemeinnovenso.sty",
    "beamerthemeinnovenso.sty"
  ).map(name => LatexLibraryFile(resourcePath = name))
  val imageFiles: List[LatexLibraryFile] =
    List(
      "logo.pdf",
      "tagline_white.pdf",
      "title_background.jpg",
      "tagline_black.pdf"
    ).map(name =>
      LatexLibraryFile(
        resourcePath = name,
        targetFileName = Some(s"images/$name")
      )
    )
  val files: List[LatexLibraryFile] = styleFiles ++ imageFiles
}
