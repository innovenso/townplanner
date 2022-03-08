package com.innovenso.townplanner.model.samples

import com.innovenso.townplanner.model.EnterpriseArchitecture
import com.innovenso.townplanner.model.concepts.{
  Actor,
  ArchitectureBuildingBlock,
  BusinessCapability,
  Database,
  Enterprise,
  Framework,
  ItPlatform,
  ItSystem,
  ItSystemIntegration,
  Language,
  Microservice,
  Queue,
  WebUI
}
import com.innovenso.townplanner.model.concepts.properties.{
  BeInvestedIn,
  BeTolerated,
  Decommissioned,
  Description,
  FatherTime,
  GoneToProduction,
  StartedDevelopment
}
import com.innovenso.townplanner.model.concepts.relationships.{
  Flow,
  Relationship
}
import com.innovenso.townplanner.model.language.Element
import com.innovenso.townplanner.model.meta.Day

import java.security.SecureRandom
import java.util.{Locale, UUID}
import com.thedeanda.lorem.LoremIpsum

import java.awt.Frame

case class SampleFactory(ea: EnterpriseArchitecture) {
  private val random = new SecureRandom()
  private val lorem = LoremIpsum.getInstance()

  def locale: Locale = {
    val index = randomInt(Locale.getAvailableLocales.length)
    Locale.getAvailableLocales.toList(index)
  }

  def randomInt(bound: Int): Int = {
    val theRandomInt = random.nextInt(bound)
    println(theRandomInt)
    if (theRandomInt == 0) 1 else theRandomInt
  }

  def url: String = lorem.getUrl

  def email: String = lorem.getEmail

  def word: String = lorem.getWords(1)

  def description: String = lorem.getWords(5, 100)

  def id: String = UUID.randomUUID().toString.replace("-", "_")

  def title: String = lorem.getTitle(1, 3)

  def name: String = lorem.getNameFemale

  def actor: Actor = ea describes Actor(title = title) as { it =>
    it has Description(description)
  }

  def language: Language = ea describes Language(title = title) as { it =>
    it has Description(description)
    it should BeInvestedIn()
  }

  def framework: Framework = ea describes Framework(title = title) as { it =>
    it has Description(description)
    it should BeTolerated()
  }

  def microservice(system: ItSystem): Microservice = {
    val implementingLanguage = language
    val implementingFramework = framework
    ea describes Microservice(title = title) as { it =>
      it has Description(description)
      it isImplementedBy implementingLanguage
      it isImplementedBy implementingFramework
      it isPartOf system
    }
  }

  def database(system: ItSystem): Database = {
    val implementingLanguage = language
    ea describes Database(title = title) as { it =>
      it has Description(description)
      it isImplementedBy implementingLanguage
      it isPartOf (system)
    }
  }

  def queue(system: ItSystem): Queue = {
    val implementingLanguage = language
    ea describes Queue(title = title) as { it =>
      it has Description(description)
      it isImplementedBy implementingLanguage
      it isPartOf (system)
    }
  }

  def ui(system: ItSystem): WebUI = {
    val implementingLanguage = language
    val implementingFramework = framework
    ea describes WebUI(title = title) as { it =>
      it has Description(description)
      it isImplementedBy implementingLanguage
      it isImplementedBy implementingFramework
      it isPartOf system
    }
  }

  def enterprise: Enterprise = ea describes Enterprise(title = title) as { it =>
    it has Description(description)
  }

  def capability(
      servedEnterprise: Option[Enterprise] = None,
      parentCapability: Option[BusinessCapability] = None
  ): BusinessCapability =
    ea describes BusinessCapability(title = title) as { it =>
      it has Description(description)
      if (servedEnterprise.isDefined) it serves servedEnterprise.get
      if (parentCapability.isDefined) it serves parentCapability.get
    }

  def buildingBlock(
      realizedCapability: Option[BusinessCapability] = None
  ): ArchitectureBuildingBlock =
    ea describes ArchitectureBuildingBlock(title = title) as { it =>
      it has Description(description)
      if (realizedCapability.isDefined) it realizes realizedCapability.get
    }

  def platform(
      realizedBuildingBlock: Option[ArchitectureBuildingBlock] = None
  ): ItPlatform =
    ea describes ItPlatform(title = title) as { it =>
      it has Description(description)
      if (realizedBuildingBlock.isDefined) it realizes realizedBuildingBlock.get
    }

  def system(
      withContainers: Boolean = true,
      containingPlatform: Option[ItPlatform] = None,
      realizedBuildingBlock: Option[ArchitectureBuildingBlock] = None,
      fatherTime: Set[FatherTime] = Set()
  ): ItSystem = {
    val theSystem = ea describes ItSystem(title = title) as { it =>
      it has Description(description)

      if (containingPlatform.isDefined) {
        it isPartOf containingPlatform.get
      }

      if (realizedBuildingBlock.isDefined) {
        it realizes realizedBuildingBlock.get
      }

      fatherTime.foreach(f => it has f on f.date)

    }

    if (withContainers) {
      val ms1 = microservice(theSystem)
      val ms2 = microservice(theSystem)
      val u = ui(theSystem)
      val q = queue(theSystem)
      val d = database(theSystem)

      ea hasRelationship Flow(source = u.key, target = ms1.key, title = title)
      ea hasRelationship Flow(source = ms1.key, target = ms2.key, title = title)
      ea hasRelationship Flow(source = ms1.key, target = d.key, title = title)
      ea hasRelationship Flow(source = ms2.key, target = q.key, title = title)
    }

    theSystem
  }

  def flow(source: Element, target: Element): Relationship =
    ea hasRelationship Flow(
      source = source.key,
      target = target.key,
      title = title
    )

  def integration(
      system1: ItSystem,
      system2: ItSystem,
      fatherTime: Set[FatherTime] = Set()
  ): ItSystemIntegration =
    ea describes ItSystemIntegration(title =
      title
    ) between system1 and system2 as { it =>
      it has Description(description)
      fatherTime.foreach(f => it has f on f.date)
    }

  def goneToProduction(year: Int, month: Int, day: Int): GoneToProduction =
    GoneToProduction(date = Day(year, month, day), description = description)

  def startedDevelopment(year: Int, month: Int, day: Int): StartedDevelopment =
    StartedDevelopment(date = Day(year, month, day), description = description)

  def decommissioned(year: Int, month: Int, day: Int): Decommissioned =
    Decommissioned(date = Day(year, month, day), description = description)
}
