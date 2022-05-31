package com.innovenso.townplanner.io.latex.slides

import com.innovenso.townplan.io.context.{Output, OutputContext}
import com.innovenso.townplan.repository.AssetRepository
import com.innovenso.townplanner.io.latex.LatexPdfWriter
import com.innovenso.townplanner.io.latex.model.LatexSpecification

object SlideDeckPdfWriter {
  def slideDecks(
      specification: LatexSpecification,
      assetRepository: AssetRepository,
      outputContext: OutputContext
  ): List[Output] =
    LatexPdfWriter(specification, assetRepository, outputContext).document
}
