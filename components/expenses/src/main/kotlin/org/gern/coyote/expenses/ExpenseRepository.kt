package org.gern.coyote.expenses

import java.math.BigDecimal
import java.time.Instant
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class ExpenseRepository {
    private val expenses: MutableMap<String, Expense> = ConcurrentHashMap()

    fun create(amount: BigDecimal, currency: Currency, instant: Instant) =
            Expense(
                    id = UUID.randomUUID().toString(),
                    amount = amount,
                    currency = currency,
                    instant = instant
            ).apply { expenses[id] = this }

    fun list() = expenses.values.toSet()
}
