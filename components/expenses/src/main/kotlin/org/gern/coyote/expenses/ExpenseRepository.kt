package org.gern.coyote.expenses

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
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

    fun find(id: UUID): ExpenseRecord? = transaction(db) {
        ExpensesTable
            .select { ExpensesTable.id eq id }
            .singleOrNull()
            ?.let(::rowToExpenseRecord)
    }

    fun list() = transaction(db) {
        ExpensesTable
            .selectAll()
            .map(::rowToExpenseRecord)
            .toSet()
    }

    private fun rowToExpenseRecord(it: ResultRow): ExpenseRecord {
        return ExpenseRecord(
            id = it[ExpensesTable.id].value,
            amount = BigInteger.valueOf(it[ExpensesTable.amount]),
            currencyCode = it[ExpensesTable.currencyCode],
            instant = Instant.ofEpochSecond(it[ExpensesTable.instant])
        )
    }
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
