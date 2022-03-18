package com.innovenso.townplanner.io.latex.document

import com.innovenso.townplan.io.context.{Output, OutputContext}
import com.innovenso.townplan.repository.AssetRepository
import com.innovenso.townplanner.io.latex.LatexPdfWriter
import com.innovenso.townplanner.io.latex.model.LatexSpecification
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.views.CompiledTechnologyRadar
import com.innovenso.townplanner.model.language.{CompiledView, View}

object DocumentPdfWriter {
  def documents(
      specification: LatexSpecification,
      assetRepository: AssetRepository,
      outputContext: OutputContext
  ): List[Output] =
    LatexPdfWriter(specification, assetRepository, outputContext).document
}
