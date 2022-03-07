package com.innovenso.townplanner.io.scenarios

import com.innovenso.townplanner.io.DiagramIO
import com.innovenso.townplanner.model.concepts.{Actor, Database, Framework, Gateway, ItSystem, Language, LanguageOrFramework, Microservice, Platform, WebUI}
import com.innovenso.townplanner.model.concepts.properties.{BeInvestedIn, BeMigrated, Description, GoneToProduction, Message, Request, Response}
import com.innovenso.townplanner.model.concepts.relationships.Flow
import com.innovenso.townplanner.model.concepts.views.FlowView
import com.innovenso.townplanner.model.meta.InTheFuture
import org.scalatest.GivenWhenThen
import org.scalatest.flatspec.AnyFlatSpec

class SelligentBirthdayCampaignSpec extends AnyFlatSpec with GivenWhenThen {
  "the proposed solution" can "be rendered" in new DiagramIO {
    Given("The Technology Radar")
    val nodejs: Language = ea has Language(title = "NodeJS")
    val docker: Framework = ea has Framework(title = "Docker")
    val openshift: Platform = ea has Platform(title = "OpenShift")
    val react: Framework = ea has Framework(title = "React")
    val mysql: Platform = ea has Platform(title = "MySQL")

    And("Marketeers and Players")
    val marketeer: Actor = ea has Actor(title = "Marketeer")
    val player: Actor = ea has Actor(title = "Player")

    And("Selligent")
    val selligent: ItSystem = ea describes ItSystem(title = "Selligent") as { it =>
      it has Description("Marketing automation platform, mainly used for segmented email campaigns")
      it should BeInvestedIn()
      it uses player
    }

    And("NGW")
    val ngw: ItSystem = ea describes ItSystem(title = "NGW - Napoleon Games Website") as { it =>
      it has Description("The Belgian Napoleon Games Website, serving 4 domains: the main napoleongames.be domain, as well as napoleoncasino.be, napoleondice.be and napoleonsports.be")
      it isUsedBy player
    }

    ea hasRelationship Flow(source = ngw.key, target = ngw.key)
    ea hasRelationship Flow(source = selligent.key, target = selligent.key)


    And("The Napoleon Games Event Bus")
    val eventBus: ItSystem = ea describes ItSystem(title = "Napoleon Games Event Bus") as { it =>
      it has Description("RabbitMQ used for all events across the NGW platform")
    }

    And("The BGC and its integration service")
    val bgc: ItSystem = ea describes ItSystem(title = "Belgian Gaming Commission") as { it =>
      it has Description("The BGC exposes a SOAP API for us to use")
    }

    val bgcIntegrationSystem: ItSystem = ea describes ItSystem(title = "BGC Integration Service") as { it =>
      it has Description("Anti-corruption layer between NGW and the Belgian Gaming Commission. Mostly an empty box right now, with all the logic still in the NGW Monolith")
      it uses bgc
    }

    val bgcIntegrationMicroservice: Microservice = ea describes Microservice(title = "BGC Microservice") as { it =>
      it has Description("Core microservice of the BGC Integration Layer")
      it uses (bgc, "integrates with, using SOAP")
      it isUsedBy ngw
      it isPartOf bgcIntegrationSystem
      it isImplementedBy nodejs
      it isImplementedBy docker
      it isImplementedBy openshift
    }

    And("The Data Intelligence Platform")
    val diPlatform: ItSystem = ea describes ItSystem(title = "Data Intelligence Platform") as { it =>
      it uses(ngw, "extracts operational data")
      it uses(selligent, "uploads player profiles")
    }

    And("The Campaign Tool")
    val campaignTool: ItSystem = ea describes ItSystem(title = "Campaign Tool") as { it =>
      it has Description("In-house developed tool for managing marketing campaigns, in 3 languages")
    }

    val communicationWorker: Microservice = ea describes Microservice(title = "Communication Worker") as { it =>
      it isPartOf campaignTool
      it has Description("Translates campaign events into communications to customers")
      it uses(eventBus, "consumes campaign events")
      it isImplementedBy nodejs
      it isImplementedBy docker
      it isImplementedBy openshift
    }

    val communicationService: Microservice = ea describes Microservice(title = "Communication Service") as { it =>
      it isPartOf campaignTool
      it has Description("Anti-corruption layer for a number of communication systems")
      it uses(selligent, "uses for email marketing")
      it isUsedBy(communicationWorker, "sends out communication")
      it isImplementedBy nodejs
      it isImplementedBy docker
      it isImplementedBy openshift
    }

    val campaignService: Microservice = ea describes Microservice(title = "Campaign Service") as { it =>
      it isPartOf campaignTool
      it has Description("Core service of the Campaign Tool")
      it uses(eventBus, "publishes campaign events and consumes journey events")
      it isImplementedBy nodejs
      it isImplementedBy docker
      it isImplementedBy openshift
      it isUsedBy ngw
      it uses ngw
    }

    val campaignDatabase: Database = ea describes Database(title="Campaign Database") as { it =>
      it has Description("Data store of the Campaign Tool")
      it isUsedBy campaignService
      it isImplementedBy mysql
    }

    val bonusTool: WebUI = ea describes WebUI(title = "Bonus Tool") as { it =>
      it has Description("Frontend and BFF for managing campaigns")
      it uses campaignService
      it isUsedBy marketeer
      it isImplementedBy nodejs
      it isImplementedBy docker
      it isImplementedBy openshift
      it isImplementedBy react
    }

    val campaignApiGateway: Gateway = ea describes Gateway(title = "Campaign API Gateway") as { it =>
      it has Description("API Gateway, exposing the Campaign API to external systems")
      it isPartOf campaignTool
      it uses(campaignService, "exposes API")
      it isUsedBy(selligent, "requests deposit for customer")
    }

    When("a flow view is requested for the BGC check")
    val bgcFlowView: FlowView = ea needs FlowView(title = "BGC Check for Birthday people") and { it =>
      it has Request("daily BGC cron job") from ngw to ngw
      it has Request("request BGC check for people with birthday tomorrow") from ngw to bgcIntegrationMicroservice
      it has Request("requests bulk BGC check") from bgcIntegrationMicroservice to bgc
      it has Response("player status") from bgc to bgcIntegrationMicroservice
      it has Response("player status") from bgcIntegrationMicroservice to ngw
      it has Request("fetches operational data, including BGC status") from diPlatform to ngw
      it has Message("sends player profile data, including BGC status") from diPlatform to selligent
    }

    val birthdayFlowView: FlowView = ea needs FlowView(title = "Birthday email campaign including bonus") and { it =>
      it has Request("select players with birthday today and BGC check passed less than 24 hours ago") from selligent to selligent
      it has Request("send birthday email") from selligent to player
      it has Request("request reward for player") from selligent to campaignApiGateway
      it has Request("pass on reward request") from campaignApiGateway to campaignService
      it has Request("claim reward") from player to ngw
      it has Request("pass on reward claim") from ngw to campaignService
      it has Request("request deposit") from campaignService to ngw
    }

    Then("diagrams are written")
    assert(diagramsAreWritten(bgcFlowView.key))
    assert(diagramsAreWritten(birthdayFlowView.key))
  }
}
