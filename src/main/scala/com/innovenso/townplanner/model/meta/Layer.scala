package com.innovenso.townplanner.model.meta

trait Layer {
  def name: String
}

object StrategyLayer extends Layer {
  val name = "Strategy"
}

object BusinessLayer extends Layer {
  val name = "Business"
}

object ApplicationLayer extends Layer {
  val name = "Application"
}

object TechnologyLayer extends Layer {
  val name = "Technology"
}
