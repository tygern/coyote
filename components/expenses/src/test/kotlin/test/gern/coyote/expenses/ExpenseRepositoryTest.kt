package test.gern.coyote.expenses

import org.gern.coyote.expenses.Expense
import org.gern.coyote.expenses.ExpenseRepository
import java.math.BigDecimal
import java.time.Instant
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ExpenseRepositoryTest {
    private val repo = ExpenseRepository()

    @Test
    fun testCreate() {
        val expense = repo.create(
                amount = BigDecimal.valueOf(34.56),
                currency = Currency.getInstance("EUR"),
                instant = Instant.ofEpochSecond(1575132999L)
        )

        val result = repo.list()

        assertEquals(setOf(Expense(
                id = expense.id,
                amount = BigDecimal.valueOf(34.56),
                currency = Currency.getInstance("EUR"),
                instant = Instant.ofEpochSecond(1575132999L)
        )), result)
    }

    @Test
    fun testCreateReturn() {
        val expense = repo.create(
                amount = BigDecimal.valueOf(34.56),
                currency = Currency.getInstance("EUR"),
                instant = Instant.ofEpochSecond(1575132999L)
        )

        assertEquals(Expense(
                id = expense.id,
                amount = BigDecimal.valueOf(34.56),
                currency = Currency.getInstance("EUR"),
                instant = Instant.ofEpochSecond(1575132999L)
        ), expense)
    }
}
