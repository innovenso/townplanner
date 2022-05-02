package com.innovenso.townplanner.io.latex.picture.model

import com.innovenso.townplanner.io.latex.model.{Text, TikzStyleInstruction}
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
  ): List[TikzStyleInstruction] = List(Text(levelColor(level)))

  def levelColor(level: KnowledgeLevel): String = level match {
    case NoKnowledge         => "innovensoRed"
    case Learner             => "innovensoRed!50"
    case Knowledgeable       => "innovensoAmber"
    case HighlyKnowledgeable => "innovensoGreen!50"
    case Expert              => "innovensoGreen"
  }
}
