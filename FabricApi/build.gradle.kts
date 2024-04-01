// gradle.properties
val fabricVersion: String by extra
val fabricLoaderVersion: String by extra

architectury {
	// Set up Architectury for Fabric.
	fabric()
}

dependencies {
	modImplementation("net.fabricmc:fabric-loader:${fabricLoaderVersion}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${fabricVersion}")
	api(project(":CommonApi", configuration = "namedElements"))
}