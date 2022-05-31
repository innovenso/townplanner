package com.innovenso.townplanner.io.latex.model

import com.innovenso.townplan.io.context.Output
import com.innovenso.townplanner.io.latex.LatexFormat
import com.innovenso.townplanner.model.meta.Key

import java.util.UUID

trait TikzInstruction {
  def value: String
  def print: String = s"${value};"
}

case class TikzNode(
    title: String,
    identifier: Key = Key(),
    at: Option[(Int, Int)] = None,
    orAt: Option[String] = None,
    orAtPolar: Option[(Double, Double)] = None,
    configuration: List[TikzStyleInstruction] = Nil,
    textVariants: List[TextVariant] = Nil,
    escapeTitle: Boolean = true
) extends TikzInstruction {
  private val position: String =
    at.map(pos => s"at (${pos._1}mm,${pos._2}mm)")
      .getOrElse(
        orAt
          .map(pos => s"at (${pos})")
          .getOrElse(
            orAtPolar.map(pos => s"at (${pos._1}:${pos._2})").getOrElse("")
          )
      )
  private val titleString: String =
    if (escapeTitle)
      LatexFormat
        .escapeAndApply(title, textVariants)
    else LatexFormat.apply(textVariants, title)
  val value =
    s"\\node[${configuration.map(_.value).mkString(",")}] ${position} (${identifier.camelCased}) {${titleString}}"
}

case class TikzNodeWithImage(
    output: Output,
    width: Int,
    identifier: Key = Key(),
    at: Option[(Int, Int)] = None,
    orAt: Option[String] = None,
    orAtPolar: Option[(Double, Double)] = None,
    configuration: List[TikzStyleInstruction] = Nil
) extends TikzInstruction {
  private val position: String =
    at.map(pos => s"at (${pos._1}mm,${pos._2}mm)")
      .getOrElse(
        orAt
          .map(pos => s"at (${pos})")
          .getOrElse(
            orAtPolar.map(pos => s"at (${pos._1}:${pos._2})").getOrElse("")
          )
      )
  val value =
    s"\\node[${configuration.map(_.value).mkString(",")}] ${position} (${identifier.camelCased}) {\\includegraphics[width=${width}mm]{${output.assetHashedName}}}"
}

case class TikzCoordinate(
    identifier: Key = Key(),
    at: Option[(Int, Int)] = None,
    orAt: Option[String] = None,
    orAtPolar: Option[(Double, Double)] = None
) extends TikzInstruction {
  private val position: String =
    at.map(pos => s"at (${pos._1}mm,${pos._2}mm)")
      .getOrElse(
        orAt
          .map(pos => s"at (${pos})")
          .getOrElse(
            orAtPolar.map(pos => s"at (${pos._1}:${pos._2})").getOrElse("")
          )
      )
  val value =
    s"\\coordinate (${identifier.camelCased}) ${position}"

}

case class Shape(
    coordinates: List[Key],
    shapeConfiguration: List[TikzStyleInstruction] = Nil
) extends TikzInstruction {
  val shapeConfigString: String =
    if (shapeConfiguration.isEmpty) ""
    else s"[${shapeConfiguration.map(_.value).mkString(",")}]"
  val coordinateString: String =
    coordinates.map(c => s"(${c.camelCased})").mkString(" -- ")
  val value = s"\\draw${shapeConfigString} ${coordinateString} --cycle"
}

case class TikzLineBetween(
    origin: String,
    target: String,
    title: Option[String] = None,
    titleConfiguration: List[TikzStyleInstruction] = Nil,
    lineConfiguration: List[TikzStyleInstruction] = Nil
) extends TikzInstruction {
  val titleNode: String = title
    .map(t =>
      s"node [${titleConfiguration.map(_.value).mkString(",")}] {${LatexFormat.escape(t)}}"
    )
    .getOrElse("")
  val lineConfigString: String =
    if (lineConfiguration.isEmpty) ""
    else s"[${lineConfiguration.map(_.value).mkString(",")}]"
  val value =
    s"\\draw${lineConfigString} (${origin}) -- (${target}) $titleNode"

}

case class TikzLine(
    fromX: Int,
    fromY: Int,
    toX: Int,
    toY: Int,
    title: Option[String] = None,
    titleConfiguration: List[TikzStyleInstruction] = Nil
) extends TikzInstruction {
  val titleNode: String = title
    .map(t =>
      s"node [${titleConfiguration.map(_.value).mkString(",")}] {${LatexFormat.escape(t)}}"
    )
    .getOrElse("")
  val value =
    s"\\draw (${fromX}mm,${fromY}mm) -- (${toX}mm,${toY}mm) $titleNode"
}

case class TikzThis(value: String) extends TikzInstruction
