plugins {
    application
}

val mainClass = "org.gern.coyote.AppKt"

application {
    mainClassName = mainClass
}

val ktorVersion: String by extra
val mariaDbVersion: String by extra
val exposedVersion: String by extra

dependencies {
    implementation(project(":components:expenses"))

    implementation("io.ktor:ktor-server-jetty:$ktorVersion")
    implementation("io.ktor:ktor-locations:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.2.3")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.mariadb.jdbc:mariadb-java-client:$mariaDbVersion")
    implementation("com.zaxxer:HikariCP:3.4.1")

    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
}

tasks.named<JavaExec>("run") {
    environment(
        "JDBC_URL" to "jdbc:mysql://localhost:3306/coyote_dev?user=coyote"
    )
}

tasks.named<Test>("test") {
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
