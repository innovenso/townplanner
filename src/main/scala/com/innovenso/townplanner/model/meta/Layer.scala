package com.innovenso.townplanner.model.meta

trait Layer {
  def name: String
}

object Business extends Layer {
  val name = "Business"
}

object Application extends Layer {
  val name = "Application"
}

object Technology extends Layer {
  val name = "Technology"
}
