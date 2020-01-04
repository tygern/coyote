package org.gern.coyotedatabase

import org.flywaydb.core.Flyway
import org.mariadb.jdbc.MariaDbDataSource


fun main() {
    val jdbcUrl = requireNotNull(System.getenv("JDBC_URL"), {"Error finding JDBC_URL"})

    Flyway.configure()
        .dataSource(MariaDbDataSource(jdbcUrl))
        .outOfOrder(false)
        .load()
        .migrate()
}
