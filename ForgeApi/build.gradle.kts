// gradle.properties
val forgeVersion: String by extra
val minecraftVersion: String by extra

architectury {
	// Set up Architectury for Forge.
	forge()
}

dependencies {
	forge("net.minecraftforge:forge:${minecraftVersion}-${forgeVersion}")
	api(project(":CommonApi", configuration = "namedElements"))
}
