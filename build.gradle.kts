import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.61" apply false
}

subprojects kotlinConfig@{
    if (name == "applications" || name == "components") return@kotlinConfig

    extra.apply {
        set("ktorVersion", "1.2.6")
    }

    group = "org.gern.coyote"

    apply(plugin = "kotlin")

    repositories {
        jcenter()
    }

    dependencies {
        "implementation"(kotlin("stdlib-jdk8"))
        "testImplementation"(kotlin("test-junit"))
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
}
