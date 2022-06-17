package com.innovenso.townplanner.io.latex.document

import com.innovenso.townplan.io.context.{Output, OutputContext, Pdf, Success}
import com.innovenso.townplan.io.state.StateRepository
import com.innovenso.townplan.repository.AssetRepository
import com.innovenso.townplanner.io.latex.LatexPdfWriter
import com.innovenso.townplanner.io.latex.model.LatexSpecification
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.views.CompiledTechnologyRadar
import com.innovenso.townplanner.model.language.{CompiledView, View}

import java.io.File

object DocumentPdfWriter {
  def documents(
      specification: LatexSpecification,
      assetRepository: AssetRepository,
      outputContext: OutputContext,
      stateRepository: StateRepository
  ): List[Output] = {
    val assetName = specification.assetName
    val existingFile: Option[File] = assetRepository.read(assetName)
    println(
      s"checking for existing, unchanged document ${assetName}: ${existingFile.map(_.getAbsolutePath).getOrElse("none")}"
    )
    if (
      existingFile.isDefined && existingFile.get.exists() && !stateRepository
        .hasChanged(specification.view, "documents")
    )
      List(
        Output(
          view = specification.view.view,
          relatedModelComponents = specification.relatedModelComponents,
          result = Success,
          assetName = Some(specification.assetName),
          fileType = Pdf,
          outputType = specification.outputType,
          day = specification.view.pointInTime
        )
      )
    else {
      val outputs =
        LatexPdfWriter(specification, assetRepository, outputContext).document
      stateRepository.touch(specification.view, "documents")
      outputs
    }
  }
}
