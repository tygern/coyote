package org.gern.coyote.expenses

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.util.*

class ExpenseService(private val repo: ExpenseRepository) {
    fun create(amount: BigDecimal, currency: Currency, instant: Instant): Expense {
        val integerAmount = amount.multiply(offsetFactor(currency))

        val record = repo.create(
            amount = integerAmount.toBigInteger(),
            currencyCode = currency.currencyCode,
            instant = instant
        )

        return Expense(
            id = record.id,
            amount = amount.setScale(currency.defaultFractionDigits, RoundingMode.HALF_UP),
            currency = currency,
            instant = instant
        )
    }

    fun list(): Set<Expense> = repo.list().map {
        val currency = Currency.getInstance(it.currencyCode)

        Expense(
            id = it.id,
            amount = it.amount.toBigDecimal().divide(offsetFactor(currency)),
            currency = currency,
            instant = it.instant
        )
    }.toSet()

    private fun offsetFactor(currency: Currency) = BigDecimal(10).pow(currency.defaultFractionDigits)
}
