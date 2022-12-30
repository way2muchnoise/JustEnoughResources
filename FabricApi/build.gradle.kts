plugins {
	java
	id("fabric-loom") version ("1.0-SNAPSHOT")
}

// gradle.properties
val fabricVersion: String by extra
val fabricLoaderVersion: String by extra
val mappingsParchmentMinecraftVersion: String by extra
val mappingsParchmentVersion: String by extra
val minecraftVersion: String by extra
val modJavaVersion: String by extra

val dependencyProjects: List<Project> = listOf(
	project(":CommonApi"),
)

dependencyProjects.forEach {
	project.evaluationDependsOn(it.path)
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
	withSourcesJar()
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(modJavaVersion))
	}
}

repositories {
	maven("https://maven.parchmentmc.org/")
}

dependencies {
	minecraft("com.mojang:minecraft:${minecraftVersion}")
	mappings(loom.layered {
		officialMojangMappings()
		parchment("org.parchmentmc.data:parchment-${mappingsParchmentMinecraftVersion}:${mappingsParchmentVersion}@zip")
	})
	modImplementation("net.fabricmc:fabric-loader:${fabricLoaderVersion}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${fabricVersion}")
	dependencyProjects.forEach {
		implementation(it)
	}
}

loom {
	// remapArchives.set(false) TODO: find out what replaces this or if we still need it
}