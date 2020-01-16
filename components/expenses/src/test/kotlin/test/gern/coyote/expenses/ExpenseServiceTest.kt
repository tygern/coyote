package test.gern.coyote.expenses

import io.mockk.*
import org.gern.coyote.expenses.Expense
import org.gern.coyote.expenses.ExpenseRecord
import org.gern.coyote.expenses.ExpenseRepository
import org.gern.coyote.expenses.ExpenseService
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ExpenseServiceTest {
    private val repo = mockk<ExpenseRepository>()
    private val service = ExpenseService(repo)

    @BeforeTest
    fun setUp() {
        clearAllMocks()

        every {
            repo.create(any(), any(), any())
        } returns
            ExpenseRecord(
                id = UUID.fromString("ce5cf31d-c466-4ad6-b765-711e0750d2c9"),
                amount = BigInteger.valueOf(3467L),
                currencyCode = "EUR",
                instant = Instant.ofEpochSecond(1235)
            )
    }

    @Test
    fun testCreate() {
        service.create(
            amount = BigDecimal(34.67),
            currency = Currency.getInstance("EUR"),
            instant = Instant.ofEpochSecond(1235)
        )

        verify {
            repo.create(
                amount = BigInteger.valueOf(3467L),
                currencyCode = "EUR",
                instant = Instant.ofEpochSecond(1235)
            )
        }
    }

    @Test
    fun testCreateReturn() {
        val result = service.create(
            amount = BigDecimal(34.67),
            currency = Currency.getInstance("EUR"),
            instant = Instant.ofEpochSecond(1235)
        )

        assertEquals(Expense(
            id = UUID.fromString("ce5cf31d-c466-4ad6-b765-711e0750d2c9"),
            amount = BigDecimal.valueOf(34.67),
            currency = Currency.getInstance("EUR"),
            instant = Instant.ofEpochSecond(1235)
        ), result)
    }

    @Test
    fun testFind() {
        every {
            repo.find(UUID.fromString("ce5cf31d-c466-4ad6-b765-711e0750d2c9"))
        } returns
            ExpenseRecord(
                id = UUID.fromString("ce5cf31d-c466-4ad6-b765-711e0750d2c9"),
                amount = BigInteger.valueOf(3467L),
                currencyCode = "EUR",
                instant = Instant.ofEpochSecond(1235)
            )

        val result = service.find("ce5cf31d-c466-4ad6-b765-711e0750d2c9")

        assertEquals(Expense(
            id = UUID.fromString("ce5cf31d-c466-4ad6-b765-711e0750d2c9"),
            amount = BigDecimal.valueOf(34.67),
            currency = Currency.getInstance("EUR"),
            instant = Instant.ofEpochSecond(1235)
        ), result)
    }

    @Test
    fun testFindBadUUID() {
        assertNull(service.find("potato"))
    }

    @Test
    fun testDelete() {
        every { repo.delete(any()) } just Runs

        service.delete("ce5cf31d-c466-4ad6-b765-711e0750d2c9")

        verify {
            repo.delete(UUID.fromString("ce5cf31d-c466-4ad6-b765-711e0750d2c9"))
        }
    }

    @Test
    fun testDeleteBadUUID() {
        service.delete("potato")
    }

    @Test
    fun testList() {
        every {
            repo.list()
        } returns setOf(
            ExpenseRecord(
                id = UUID.fromString("ce5cf31d-c466-4ad6-b765-711e0750d2c9"),
                amount = BigInteger.valueOf(3467L),
                currencyCode = "EUR",
                instant = Instant.ofEpochSecond(1235)
            ),
            ExpenseRecord(
                id = UUID.fromString("aaacf31d-c466-4ad6-b765-711e0750d2c9"),
                amount = BigInteger.valueOf(8763L),
                currencyCode = "JPY",
                instant = Instant.ofEpochSecond(6798)
            )
        )

        val result = service.list()

        assertEquals(
            setOf(
                Expense(
                    id = UUID.fromString("ce5cf31d-c466-4ad6-b765-711e0750d2c9"),
                    amount = BigDecimal.valueOf(34.67),
                    currency = Currency.getInstance("EUR"),
                    instant = Instant.ofEpochSecond(1235)
                ),
                Expense(
                    id = UUID.fromString("aaacf31d-c466-4ad6-b765-711e0750d2c9"),
                    amount = BigDecimal.valueOf(8763),
                    currency = Currency.getInstance("JPY"),
                    instant = Instant.ofEpochSecond(6798)
                )
            ),
            result
        )
    }
}
