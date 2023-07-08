plugins {
	java
	id("net.minecraftforge.gradle") version ("[6.0,6.2)")
	id("org.parchmentmc.librarian.forgegradle") version ("1.+")
}

// gradle.properties
val forgeVersion: String by extra
val mappingsChannel: String by extra
val mappingsParchmentMinecraftVersion: String by extra
val mappingsParchmentVersion: String by extra
val minecraftVersion: String by extra
val modJavaVersion: String by extra

val dependencyProjects: List<ProjectDependency> = listOf(
	project.dependencies.project(":CommonApi"),
)

dependencyProjects.forEach {
	project.evaluationDependsOn(it.dependencyProject.path)
}

sourceSets {
	named("main") {
		resources {
			//The API has no resources
			setSrcDirs(emptyList<String>())
		}
	}
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(modJavaVersion))
	}
}

dependencies {
	"minecraft"(
		group = "net.minecraftforge",
		name = "forge",
		version = "${minecraftVersion}-${forgeVersion}"
	)
	dependencyProjects.forEach {
		implementation(it)
	}
}

minecraft {
	mappings(mappingsChannel, "${mappingsParchmentMinecraftVersion}-${mappingsParchmentVersion}-${minecraftVersion}")
	//mappings("official", minecraftVersion)

	copyIdeResources.set(true)

	// All minecraft configurations in the multi-project must be identical, including ATs,
	// because of a ForgeGradle bug https://github.com/MinecraftForge/ForgeGradle/issues/844
	accessTransformer(file("../Forge/src/main/resources/META-INF/accesstransformer.cfg"))

	// no runs are configured for API
}