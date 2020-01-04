val ktorVersion : String by extra
val exposedVersion : String by extra
val mariaDbVersion : String by extra

dependencies {
    implementation("io.ktor:ktor-locations:$ktorVersion")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

    testImplementation("org.mariadb.jdbc:mariadb-java-client:$mariaDbVersion")
}

tasks.named<Test>("test") {
    environment(
        "JDBC_URL" to "jdbc:mysql://localhost:3306/coyote_test?user=coyote"
    )
}
