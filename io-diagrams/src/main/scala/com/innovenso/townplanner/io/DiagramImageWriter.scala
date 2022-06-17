package com.innovenso.townplanner.io

import com.innovenso.townplan.io.context._
import com.innovenso.townplan.io.state.StateRepository
import com.innovenso.townplan.repository.AssetRepository
import com.innovenso.townplanner.io.context.PlantUMLDiagram
import com.innovenso.townplanner.io.model.DiagramSpecification
import net.sourceforge.plantuml.{
  FileFormat,
  FileFormatOption,
  SourceStringReader
}

import java.io.{BufferedWriter, File, FileOutputStream, FileWriter, PrintWriter}
import java.nio.file.Files
import java.util.UUID

object DiagramImageWriter {
  def diagrams(
      specification: DiagramSpecification,
      assetRepository: AssetRepository,
      stateRepository: StateRepository
  ): List[Output] = {
    println(s"getting diagrams for ${specification.view.title}")
    val outputs =
      List((FileFormat.SVG, Svg), (FileFormat.EPS, Eps), (FileFormat.PNG, Png))
        .map(ff =>
          getOrCreateDiagram(
            specification,
            assetRepository,
            ff._1,
            ff._2,
            stateRepository
          )
        )
    stateRepository.touch(specification.view, "diagrams")
    outputs
  }

  private def getOrCreateDiagram(
      specification: DiagramSpecification,
      assetRepository: AssetRepository,
      fileFormat: FileFormat,
      outputFileType: OutputFileType,
      stateRepository: StateRepository
  ): Output = {
    val assetName = specification.assetName(outputFileType)
    val existingFile: Option[File] = assetRepository.read(assetName)
    if (
      existingFile.isDefined && existingFile.get.exists() && !stateRepository
        .hasChanged(specification.view, "diagrams")
    )
      Output(
        view = specification.view.view,
        result = Success,
        assetName = Some(assetName),
        fileType = outputFileType,
        outputType = specification.outputType,
        day = specification.view.pointInTime,
        relatedModelComponents = specification.relatedModelComponents
      )
    else diagram(specification, assetRepository, fileFormat, outputFileType)
  }

  private def diagram(
      specification: DiagramSpecification,
      assetRepository: AssetRepository,
      fileFormat: FileFormat,
      outputFileType: OutputFileType
  ): Output = {
    println(s"creating diagram for ${specification.view.title}")
    val outputFile =
      File.createTempFile(UUID.randomUUID.toString, fileFormat.getFileSuffix)

    val specificationReader = new SourceStringReader(
      specification.plantumlSpecification
    )
    val outputStream = new FileOutputStream(outputFile)
    try {
      specificationReader.outputImage(
        outputStream,
        new FileFormatOption(fileFormat)
      )
      val assetName = specification.assetName(outputFileType)
      assetRepository.write(outputFile, assetName)
      Output(
        view = specification.view.view,
        result = Success,
        assetName = Some(assetName),
        fileType = outputFileType,
        outputType = specification.outputType,
        day = specification.view.pointInTime,
        relatedModelComponents = specification.relatedModelComponents
      )
    } catch {
      case error: Throwable =>
        Output(
          view = specification.view.view,
          result = Failure(error.getMessage),
          assetName = None,
          fileType = outputFileType,
          outputType = specification.outputType,
          day = specification.view.pointInTime
        )
    } finally if (outputStream != null) outputStream.close()
  }
}
