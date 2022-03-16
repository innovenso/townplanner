package com.innovenso.townplanner.io.latex.model

import com.innovenso.townplan.io.context.Output
import com.innovenso.townplanner.model.language.{CompiledView, View}

case class LatexSpecification(
    view: CompiledView[_ <: View],
    latexSourceCode: String,
    dependencies: List[Output] = Nil,
    filenameAppendix: Option[String] = None
)
