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
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.0")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
        //compileOnly("io.papermc.paper:paper-api:1.19.0-R0.1-SNAPSHOT")
        compileOnly("org.spigotmc:spigot-api:1.19-R0.1-SNAPSHOT")
    }
}