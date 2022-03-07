package com.wayneenterprises.townplan

import com.innovenso.townplan.application.EnterpriseArchitectureAsCode
import com.wayneenterprises.townplan.business.Actors
import com.wayneenterprises.townplan.strategy.{BusinessCapabilities, Enterprises}
import com.wayneenterprises.townplan.technology.TechnologyRadar

object WayneEnterprisesTownPlan extends EnterpriseArchitectureAsCode{
  implicit val enterprises: Enterprises = Enterprises()
  implicit val capabilities: BusinessCapabilities = BusinessCapabilities()
  implicit val technologyRadar: TechnologyRadar = TechnologyRadar()
  implicit val actors: Actors = Actors()

  diagrams
}
