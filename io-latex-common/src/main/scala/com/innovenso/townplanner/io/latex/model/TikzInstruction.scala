package com.innovenso.townplanner.io.latex.model

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
    at: Option[(Int, Int)] = None, orAt: Option[String] = None,
    configuration: List[TikzStyleInstruction] = Nil,
    textVariants: List[TextVariant] = Nil
) extends TikzInstruction {
  private val position: String =
    at.map(pos => s"at (${pos._1}mm,${pos._2}mm)").getOrElse(orAt.map(pos => s"at (${pos})").getOrElse(""))
  val value =
    s"\\node[${configuration.map(_.value).mkString(",")}] ${position} (${identifier.camelCased}) {${LatexFormat
        .escapeAndApply(title, textVariants)}}"
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
