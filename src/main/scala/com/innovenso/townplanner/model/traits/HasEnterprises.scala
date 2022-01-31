package com.innovenso.townplanner.model.traits

import com.innovenso.townplanner.model.TownPlan
import com.innovenso.townplanner.model.concepts.Enterprise
import com.innovenso.townplanner.model.meta.{Description, Key, SortKey, Title}

trait HasEnterprises extends HasTownPlan {
  def withEnterprise(key: Key = Key(), sortKey: SortKey = SortKey(None), title: Title, description: Description = Description(None)): TownPlan = withModelComponent(Enterprise(key = key, sortKey =
    sortKey, title = title, description = description))
}
