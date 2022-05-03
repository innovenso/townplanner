package com.innovenso.townplanner.io.latex.picture.model

import com.innovenso.townplanner.io.latex.model.{
  Circle,
  Fill,
  InnerSep,
  Text,
  TikzStyleInstruction
}
import com.innovenso.townplanner.model.concepts.relationships.{
  Expert,
  HighlyKnowledgeable,
  KnowledgeLevel,
  Knowledgeable,
  Learner,
  NoKnowledge
}

object KnowledgeMatrixLayoutHelper {
  def levelTikzConfiguration(
      level: KnowledgeLevel
  ): List[TikzStyleInstruction] =
    List(Text(textColor(level)), Fill(fillColor(level)), Circle, InnerSep(0.5))

  def fillColor(level: KnowledgeLevel): String = level match {
    case NoKnowledge         => "white"
    case Learner             => "innovensoRed!50"
    case Knowledgeable       => "innovensoAmber"
    case HighlyKnowledgeable => "innovensoGreen!50"
    case Expert              => "innovensoGreen"
  }

  def textColor(level: KnowledgeLevel): String = level match {
    case NoKnowledge         => "gray!40"
    case Learner             => "black"
    case Knowledgeable       => "black"
    case HighlyKnowledgeable => "black"
    case Expert              => "white"
  }
}
