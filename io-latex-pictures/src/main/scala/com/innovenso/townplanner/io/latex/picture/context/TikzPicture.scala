package com.innovenso.townplanner.io.latex.picture.context

import com.innovenso.townplan.io.context.OutputType

object TikzPicture extends OutputType {
  val title: String = "TikZ Picture"
  val description: String =
    "A visualization of a concept or view using PGF/TikZ, for use in LaTeX documents typically."
}
