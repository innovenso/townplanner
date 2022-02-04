package com.innovenso.townplanner.model.language

import com.innovenso.townplanner.model.meta.{Description, Title}

trait Concept extends ModelComponent {
  def title: Title
  def description: Description
}
