package test.gern.coyote.expenses

import io.ktor.locations.KtorExperimentalLocationsAPI
import org.gern.coyote.expenses.ExpensesPath
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@KtorExperimentalLocationsAPI
class ExpensesPathTest {
    @Test
    fun validWhenAllInfoPresent() {
        assertTrue(ExpensesPath(
            currency = "USD",
            amount = 89.56,
            instant = 1234
        ).valid)
    }

    @Test
    fun notValidWhenMissingCurrency() {
        assertFalse(ExpensesPath(
            amount = 89.56,
            instant = 1234
        ).valid)
    }

    @Test
    fun notValidWhenInvalidCurrency() {
        assertFalse(ExpensesPath(
            currency = "TTT",
            amount = 89.56,
            instant = 1234
        ).valid)
    }

    @Test
    fun notValidWhenMissingAmount() {
        assertFalse(ExpensesPath(
            currency = "USD",
            instant = 1234
        ).valid)
    }

    @Test
    fun notValidWhenMissingInstant() {
        assertFalse(ExpensesPath(
            currency = "USD",
            amount = 89.56
        ).valid)
    }
}
