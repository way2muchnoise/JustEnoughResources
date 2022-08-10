plugins {
    java
    id("org.spongepowered.gradle.vanilla") version "0.2.1-SNAPSHOT"
}

// gradle.properties
val minecraftVersion: String by extra

minecraft {
    version(minecraftVersion)
    // no runs are configured for Common API
    accessWideners(file(rootProject.project(":Common").projectDir.path + "/src/main/resources/jeresources.accesswidener"))
}

sourceSets {
    named("main") {
        resources {
            //The API has no resources
            setSrcDirs(emptyList<String>())
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(
        group = "org.spongepowered",
        name = "mixin",
        version = "0.8.5"
    )
}