package org.gern.coyote.expenses

import java.math.BigDecimal
import java.time.Instant
import java.util.*

data class Expense(
        val id: String,
        val amount: BigDecimal,
        val currency: Currency,
        val instant: Instant
)
