import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.61"
    application
}

repositories {
    jcenter()
}

val mainClass = "org.gern.coyote.AppKt"

application {
    mainClassName = mainClass
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile(ktor("ktor-server-jetty"))
    compile(ktor("ktor-locations"))
    compile("ch.qos.logback:logback-classic:1.2.3")
}

tasks {
    jar {
        manifest {
            attributes("Main-Class" to mainClass)
        }
        from({
            val compileConfig = configurations.compile.get()
            compileConfig.map { file ->
                { if (file.isDirectory) file else zipTree(file) }
            }
        })
    }
}

fun ktor(name: String) = "io.ktor:$name:1.2.6"
