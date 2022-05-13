package com.innovenso.townplanner.model.meta

import java.util.Currency

case class MonetaryAmount(
    amount: Double,
    currency: Currency = Currency.getInstance("EUR")
) {
  override def toString: String = f"${currency.getSymbol}${amount}%2.2f"
}
