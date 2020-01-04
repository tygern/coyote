package org.gern.coyote.expenses

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigInteger
import java.time.Instant
import java.util.*

class ExpenseRepository(private val db: Database) {
    fun create(amount: BigInteger, currencyCode: String, instant: Instant) = transaction(db) {
        ExpensesTable.insertAndGetId {
            it[ExpensesTable.amount] = amount.toLong()
            it[ExpensesTable.currencyCode] = currencyCode
            it[ExpensesTable.instant] = instant.epochSecond
        }.let {
            ExpenseRecord(
                id = it.value,
                amount = amount,
                currencyCode = currencyCode,
                instant = instant
            )
        }
    }

    fun list() = transaction(db) { ExpensesTable.selectAll().map {
        ExpenseRecord(
            id = it[ExpensesTable.id].value,
            amount = BigInteger.valueOf(it[ExpensesTable.amount]),
            currencyCode = it[ExpensesTable.currencyCode],
            instant = Instant.ofEpochSecond(it[ExpensesTable.instant])
        )
    } }.toSet()
}

private object ExpensesTable : UUIDTable() {
    val amount = long("amount")
    val currencyCode = varchar(name = "currency_code", length = 3)
    val instant = long("instant")

    override val tableName = "expenses"
}

data class ExpenseRecord(
    val id: UUID,
    val amount: BigInteger,
    val currencyCode: String,
    val instant: Instant
)
