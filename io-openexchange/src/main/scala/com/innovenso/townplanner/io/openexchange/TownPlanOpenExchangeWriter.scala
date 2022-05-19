package com.innovenso.townplanner.io.openexchange

import com.innovenso.townplan.io.context.OutputContext
import com.innovenso.townplan.repository.AssetRepository
import com.innovenso.townplanner.io.openexchange.model.TownPlanOpenExchangeExport
import com.innovenso.townplanner.model.TownPlan

import java.io.File
import java.nio.file.Files

case class TownPlanOpenExchangeWriter()(implicit
    assetRepository: AssetRepository
) {

  def write()(implicit
      townPlan: TownPlan,
      outputContext: OutputContext
  ): OutputContext = {
    outputContext.withOutput(
      TownPlanOpenExchangeExport(townPlan).output
    )
  }
}
