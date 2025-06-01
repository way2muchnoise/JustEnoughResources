pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/" )
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net/")
        maven(url = "https://maven.neoforged.net/releases/")
        gradlePluginPortal()
    }
}

rootProject.name = "JustEnoughResources"
include(
		"CommonApi", "Common",
        //"Fabric",
        //"Forge",
        "NeoForge",
)