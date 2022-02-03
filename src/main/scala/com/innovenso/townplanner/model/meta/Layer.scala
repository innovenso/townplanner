package com.innovenso.townplanner.model.meta

trait Layer {
  def name: String
}

case object StrategyLayer extends Layer {
  val name = "Strategy"
}

case object BusinessLayer extends Layer {
  val name = "Business"
}

case object ApplicationLayer extends Layer {
  val name = "Application"
}

case object TechnologyLayer extends Layer {
  val name = "Technology"
}

case object ImplementationLayer extends Layer {
  val name = "Implementation and Migration"
}
