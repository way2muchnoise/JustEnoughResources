// gradle.properties
val neoforgeVersion: String by extra

architectury {
    // Set up Architectury for NeoForge.
    neoForge()
}

repositories {
    maven("https://maven.neoforged.net/releases/")
}

dependencies {
    neoForge("net.neoforged:neoforge:${neoforgeVersion}")
    api(project(":CommonApi", configuration = "namedElements"))
}

