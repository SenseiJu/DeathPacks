plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
}

group = "me.senseiju"

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

    repositories {
        maven("https://papermc.io/repo/repository/maven-public/")
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.21")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
        compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
    }
}