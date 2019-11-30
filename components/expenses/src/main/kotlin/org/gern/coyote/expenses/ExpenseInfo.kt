package org.gern.coyote.expenses

data class ExpenseInfo(
        val id: String,
        val instant: Long,
        val amount: Double,
        val currency: String
) {
    constructor(expense: Expense) : this(
        id = expense.id,
        instant = expense.instant.epochSecond,
        amount = expense.amount.toDouble(),
        currency = expense.currency.currencyCode
    )
}
