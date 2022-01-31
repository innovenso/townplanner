package com.innovenso.townplanner.model.factory

import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.Enterprise
import com.innovenso.townplanner.model.meta.{Description, Key, SortKey, Title}

import scala.util.Try

trait CanAddEnterprises extends CanManipulateTownPlan {
  def withEnterprise(
      key: Key = Key(),
      sortKey: SortKey = SortKey(None),
      title: Title,
      description: Description = Description(None)
  ): Try[TownPlan] = withModelComponent(
    Enterprise(
      key = key,
      sortKey = sortKey,
      title = title,
      description = description
    )
  )
}
