import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "me.senseiju.deathpacks"
version = "1.0.2"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.mattstudios.me/artifactory/public/")
}

dependencies {
    implementation(project(":api"))
    implementation("com.github.SenseiJu:Sentils:be17902aca")
    implementation("dev.triumphteam:triumph-gui:3.1.2")
    implementation("me.mattstudios.utils:matt-framework:1.4.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1-native-mt")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks {
    withType(ShadowJar::class) {
        archiveFileName.set("DeathPacks-${archiveVersion.get()}.jar")

        relocate("dev.triumphteam.gui", "me.senseiju.deathpacks.shaded.gui")
        relocate("me.mattstudios.mf", "me.senseiju.deathpacks.shaded.mf")

        minimize()
    }

    processResources {
        filesMatching("plugin.yml") {
            expand("version" to version)
        }
    }

    register("copyJarToServer", Copy::class) {
        from(shadowJar)
        into("D:\\Servers\\Minecraft\\DeathPacks\\plugins\\update")
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}