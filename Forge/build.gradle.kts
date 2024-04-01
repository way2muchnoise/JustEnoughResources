import net.darkhax.curseforgegradle.TaskPublishCurseForge
import net.darkhax.curseforgegradle.Constants as CFG_Constants

plugins {
	id("net.darkhax.curseforgegradle") version("1.1.18")
	id("com.modrinth.minotaur") version("2.+")
}

// gradle.properties
val curseHomepageLink: String by extra
val curseProjectId: String by extra
val modrinthProjectId: String by extra
val forgeVersion: String by extra
val jeiVersion: String by extra
val minecraftVersion: String by extra
val modFileName: String by extra
val modJavaVersion: String by extra

val baseArchivesName = "${modFileName}-Forge-${minecraftVersion}"
base {
	archivesName.set(baseArchivesName)
}

architectury {
	// Create the IDE launch configurations for this subproject.
	platformSetupLoomIde()
	// Set up Architectury for Forge.
	forge()
}

loom {
	// Make the Forge project use the common access widener.
	accessWidenerPath.set(project(":Common").file("src/main/resources/jeresources.accesswidener"))

	forge {
		// Enable Loom's AW -> AT conversion for Forge.
		// This will make remapJar convert the common AW to a Forge access transformer.
		convertAccessWideners.set(true)
		// Add an "extra" converted access widener which comes from outside the project.
		// The path is relative to the mod jar's root.
		extraAccessWideners.add("jeresources.accesswidener")
	}
}

dependencies {
	forge("net.minecraftforge:forge:${minecraftVersion}-${forgeVersion}")

	modCompileOnlyApi("mezz.jei:jei-${minecraftVersion}-common-api:${jeiVersion}")
	modCompileOnlyApi("mezz.jei:jei-${minecraftVersion}-forge-api:${jeiVersion}")
	// at runtime, use the full JEI jar for Forge
	modRuntimeOnly("mezz.jei:jei-${minecraftVersion}-forge:${jeiVersion}") { isTransitive = false }

	implementation(project(":Common", configuration = "namedElements")) { isTransitive = false }
	shadowImplementation(project(":Common", configuration = "transformProductionForge")) { isTransitive = false }

	implementation(project(":ForgeApi", configuration = "namedElements"))
	shadowImplementation(project(":CommonApi")) { isTransitive = false }
	shadowImplementation(project(":ForgeApi")) { isTransitive = false }

	// Need to make sure the API packages are loaded while during run in IDE
	forgeRuntimeLibrary(project(":CommonApi", configuration = "namedElements"))
	forgeRuntimeLibrary(project(":ForgeApi", configuration = "namedElements"))
}

val apiJar = tasks.register<Jar>("apiJar") {
	from(project(":CommonApi").sourceSets.main.get().output)
	from(project(":ForgeApi").sourceSets.main.get().output)

	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	archiveClassifier.set("api")
}

artifacts {
	archives(apiJar.get())
	archives(tasks.remapJar.get())
	archives(tasks.remapSourcesJar.get())
}

tasks.withType<Jar> {
	destinationDirectory.set(file(rootProject.rootDir.path + "/output"))
}

tasks.withType<net.fabricmc.loom.task.RemapJarTask> {
	destinationDirectory.set(file(rootProject.rootDir.path + "/output"))
}

tasks.register<TaskPublishCurseForge>("publishCurseForge") {

	apiToken = System.getenv("CURSE_KEY") ?: "0"

	val mainFile = upload(curseProjectId, tasks.remapJar.get())
	mainFile.changelogType = CFG_Constants.CHANGELOG_MARKDOWN
	mainFile.changelog = System.getenv("CHANGELOG") ?: ""
	mainFile.releaseType = CFG_Constants.RELEASE_TYPE_ALPHA
	mainFile.addModLoader("Forge")
	mainFile.addJavaVersion("Java $modJavaVersion")
	mainFile.addGameVersion(minecraftVersion)
	//TODO: Figure out how to upload correct files
//	mainFile.withAdditionalFile(apiJar.get())
//	mainFile.withAdditionalFile(tasks.remapSourcesJar.get())
}

modrinth {
	token.set(System.getenv("MODRINTH_TOKEN") ?: "0")
	projectId.set(modrinthProjectId)
	versionNumber.set("${project.version}")
	versionName.set("${project.version} for Forge $minecraftVersion")
	versionType.set("alpha")
	changelog.set(System.getenv("CHANGELOG") ?: "")
	uploadFile.set(tasks.remapJar.get())
	gameVersions.add(minecraftVersion)
	// additionalFiles.addAll(arrayOf(apiJar.get(), sourcesJar.get())) // TODO: Figure out how to upload these
}
tasks.modrinth.get().dependsOn(tasks.remapJar)