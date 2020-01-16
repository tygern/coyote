package org.gern.coyote.expenses

import java.math.BigDecimal
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

        return expenseFromRecord(record)
    }

    fun find(id: String): Expense? = try {
        repo.find(UUID.fromString(id))
            ?.let { expenseFromRecord(it) }
    } catch (e: IllegalArgumentException) {
        null
    }

    fun list(): Set<Expense> = repo.list()
        .map(this::expenseFromRecord)
        .toSet()

    fun delete(id: String) = try {
        repo.delete(UUID.fromString(id))
    } catch (e: IllegalArgumentException) {
    }

    private fun expenseFromRecord(record: ExpenseRecord): Expense {
        val currency = Currency.getInstance(record.currencyCode)

        return Expense(
            id = record.id,
            amount = record.amount.toBigDecimal().divide(offsetFactor(currency)),
            currency = currency,
            instant = record.instant
        )
    }

    private fun offsetFactor(currency: Currency) = BigDecimal(10).pow(currency.defaultFractionDigits)
}
