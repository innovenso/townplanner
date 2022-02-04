package com.innovenso.townplanner.model.concepts.properties

import com.innovenso.townplanner.model.meta._

import java.util.Currency

case class Capex(
    key: Key = Key(),
    sortKey: SortKey = SortKey(None),
    title: Title,
    description: Description = Description(None),
    category: Category = Category(None),
    fiscalYear: Year = ThisYear,
    numberOfUnits: UnitCount,
    unitOfMeasure: UnitOfMeasure,
    costPerUnit: MonetaryAmount
) extends Cost

case class Opex(
    key: Key = Key(),
    sortKey: SortKey = SortKey(None),
    title: Title,
    description: Description = Description(None),
    category: Category = Category(None),
    fiscalYear: Year = ThisYear,
    numberOfUnits: UnitCount,
    unitOfMeasure: UnitOfMeasure,
    costPerUnit: MonetaryAmount
) extends Cost

trait Cost extends Property {
  val canBePlural: Boolean = true
  def title: Title
  def description: Description
  def category: Category
  def fiscalYear: Year
  def numberOfUnits: UnitCount
  def unitOfMeasure: UnitOfMeasure
  def costPerUnit: MonetaryAmount
  def totalCost: MonetaryAmount = MonetaryAmount(
    numberOfUnits.count * costPerUnit.amount,
    costPerUnit.currency
  )
}

trait HasCosts extends HasProperties {
  def costs(fiscalYear: Year): List[Cost] =
    costs.filter(_.fiscalYear == fiscalYear)

  def costs: List[Cost] = props(classOf[Cost])

  def costFiscalYears: List[Year] =
    costs.map(_.fiscalYear).distinct.sortWith(_.value < _.value)

  def totalCapex(fiscalYear: Year, currency: Currency): MonetaryAmount =
    MonetaryAmount(
      capex(fiscalYear)
        .filter(_.costPerUnit.currency == currency)
        .map(_.totalCost.amount)
        .sum,
      currency
    )

  def capex(fiscalYear: Year): List[Capex] =
    capex.filter(_.fiscalYear == fiscalYear)

  def capex: List[Capex] = props(classOf[Capex])

  def totalOpex(fiscalYear: Year, currency: Currency): MonetaryAmount =
    MonetaryAmount(
      opex(fiscalYear)
        .filter(_.costPerUnit.currency == currency)
        .map(_.totalCost.amount)
        .sum,
      currency
    )

  def opex(fiscalYear: Year): List[Opex] =
    opex.filter(_.fiscalYear == fiscalYear)

  def opex: List[Opex] = props(classOf[Opex])
}
