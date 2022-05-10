package com.innovenso.townplanner.io

import cats.effect.IO
import com.innovenso.townplan.io.context.{Failure, Html, Output, OutputContext, Pdf, Png, Success, Svg}
import com.innovenso.townplan.repository.AssetRepository
import com.innovenso.townplanner.io.context.{Diagram, WebPage}
import com.innovenso.townplanner.io.model.{HtmlSpecification, StaticAssetSpecification}
import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.views.SingleElementView
import com.innovenso.townplanner.model.language.Element
import org.apache.commons.io.FileUtils
import com.innovenso.townplanner.model.meta.{Layer, Today}
import website.html.ElementPage

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.UUID

case class TownPlanWebsiteWriter()(implicit assetRepository: AssetRepository) {
  def write()(implicit townPlan: TownPlan, outputContext: OutputContext): OutputContext =
    outputContext.withOutputs(workingDirectory.map(dir => {
      staticFiles()
      html()
    }).getOrElse(Nil))

  def workingDirectory: Option[File] = Some(Files.createTempDirectory("TownplannerWebsite").toFile)
  val targetDirectory: File = Files.createTempDirectory("TownplannerWebsiteOutput").toFile

  def staticFiles()(implicit outputContext: OutputContext, townPlan: TownPlan): Unit = {
    outputContext.outputsOfFileType(Png).foreach(copyStaticFile)
    outputContext.outputsOfFileType(Svg).foreach(copyStaticFile)
    outputContext.outputsOfFileType(Pdf).foreach(copyStaticFile)
  }

  def copyStaticFile(output: Output): Unit = output.assetName
    .flatMap(assetRepository.read)
    .map(file => StaticAssetSpecification( file, output)).foreach(specification => assetRepository.write(specification.file, specification.assetName)
  )

  def html()(implicit outputContext: OutputContext, townPlan: TownPlan): List[Output] = {
    townPlan.components(classOf[Element]).map(element => HtmlSpecification(element, navigationContext => ElementPage(element, navigationContext, townPlan, outputContext).body)).map(writeHtml)
  }

  def writeHtml(htmlSpecification: HtmlSpecification): Output = {
    val outputFile =
      File.createTempFile(UUID.randomUUID.toString, Html.extension )

    try {
      FileUtils.writeStringToFile(outputFile, htmlSpecification.html, StandardCharsets.UTF_8)
      assetRepository.write(outputFile, htmlSpecification.assetName)
      Output(
        view = SingleElementView(htmlSpecification.element), relatedModelComponents = List(htmlSpecification.element), result = Success, assetName = Some(htmlSpecification.assetName), fileType = Html, outputType = WebPage, day = Today
      )
    } catch {
      case error: Throwable => Output(
        view = SingleElementView(htmlSpecification.element),
        result = Failure(error.getMessage),
        assetName = None,
        fileType = Html,
        outputType = WebPage,
        day = Today
      )
    }

  }

}
