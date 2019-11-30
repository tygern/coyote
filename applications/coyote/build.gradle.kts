plugins {
    application
}

val mainClass = "org.gern.coyote.AppKt"

application {
    mainClassName = mainClass
}

val ktorVersion : String by extra

dependencies {
    implementation(project(":components:expenses"))

    implementation("io.ktor:ktor-server-jetty:$ktorVersion")
    implementation("io.ktor:ktor-locations:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.2.3")

    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("junit:junit:4.12")
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
