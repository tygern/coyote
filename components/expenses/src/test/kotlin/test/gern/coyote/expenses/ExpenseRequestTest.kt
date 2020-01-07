package test.gern.coyote.expenses

import io.ktor.locations.KtorExperimentalLocationsAPI
import org.gern.coyote.expenses.ExpensePath
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@KtorExperimentalLocationsAPI
class ExpensePathTest {
    @Test
    fun validWhenAllInfoPresent() {
        assertTrue(ExpensePath(
            currency = "USD",
            amount = 89.56,
            instant = 1234
        ).valid)
    }

    @Test
    fun notValidWhenMissingCurrency() {
        assertFalse(ExpensePath(
            amount = 89.56,
            instant = 1234
        ).valid)
    }

    @Test
    fun notValidWhenInvalidCurrency() {
        assertFalse(ExpensePath(
            currency = "TTT",
            amount = 89.56,
            instant = 1234
        ).valid)
    }

    @Test
    fun notValidWhenMissingAmount() {
        assertFalse(ExpensePath(
            currency = "USD",
            instant = 1234
        ).valid)
    }

    @Test
    fun notValidWhenMissingInstant() {
        assertFalse(ExpensePath(
            currency = "USD",
            amount = 89.56
        ).valid)
    }
}
