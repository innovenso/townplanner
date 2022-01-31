package com.innovenso.townplanner.model.language

import com.innovenso.townplanner.model.meta.{Key, SortKey}

trait ModelComponent {
  def key: Key
  def sortKey: SortKey

}
