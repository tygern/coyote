val mainClass = "org.gern.coyotedatabase.MigrateKt"

dependencies {
    implementation("org.flywaydb:flyway-core:6.1.0")
    implementation("org.mariadb.jdbc:mariadb-java-client:2.5.2")
}

tasks.register<JavaExec>("devMigrate") {
    group = "Migrations"
    description = "Migrate the development database"
    main = mainClass
    classpath = sourceSets["main"].runtimeClasspath

    dependsOn(getTasksByName("classes", false))
    environment(
        "JDBC_URL" to "jdbc:mysql://localhost:3306/coyote_dev?user=coyote"
    )
}

tasks.register<JavaExec>("testMigrate") {
    group = "Migrations"
    description = "Migrate the test database"
    main = mainClass
    classpath = sourceSets["main"].runtimeClasspath

    dependsOn(getTasksByName("classes", false))
    environment(
        "JDBC_URL" to "jdbc:mysql://localhost:3306/coyote_test?user=coyote"
    )
}

tasks {
    jar {
        manifest {
            attributes("Main-Class" to mainClass)
        }

        from({
            configurations.compileClasspath.get()
                .filter { it.name.endsWith("jar") }
                .map { zipTree(it) }
        })
    }
}
