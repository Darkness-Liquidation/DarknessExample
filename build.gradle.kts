plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "ac.darkness.darknessac"
version = "1.0"

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")
    compileOnly("com.github.retrooper:packetevents-spigot:2.14.0")
    compileOnly("com.github.Darkness-Liquidation:DarknessAPI:1.0.1")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks {
    runServer {
        minecraftVersion("1.20.6")
        jvmArgs("-Xms2G", "-Xmx2G")
    }

    processResources {
        val props = mapOf("version" to (project.version.toString().takeIf { it != "unspecified" } ?: "1.0.0"))
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}
