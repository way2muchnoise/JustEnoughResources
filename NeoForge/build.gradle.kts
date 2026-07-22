import net.darkhax.curseforgegradle.TaskPublishCurseForge
import net.darkhax.curseforgegradle.Constants as CFG_Constants

plugins {
    id("net.darkhax.curseforgegradle") version("1.1.27")
    id("com.modrinth.minotaur") version("2.+")
}

// gradle.properties
val curseHomepageLink: String by extra
val curseProjectId: String by extra
val modrinthProjectId: String by extra
val neoforgeVersion: String by extra
val minecraftVersion: String by extra
val jeiVersion: String by extra
val modId: String by extra
val modFileName: String by extra
val modJavaVersion: String by extra
val mappingsParchmentMinecraftVersion: String by extra
val mappingsParchmentVersion: String by extra

val baseArchivesName = "${modFileName}-NeoForge-${minecraftVersion}"
base {
    archivesName.set(baseArchivesName)
}

architectury {
    // Create the IDE launch configurations for this subproject.
    platformSetupLoomIde()
    // Set up Architectury for Forge.
    neoForge()
}

loom {
    accessWidenerPath.set(project(":Common").file("src/main/resources/jeresources.accesswidener"))
}

repositories {
    maven("https://maven.neoforged.net/releases/")
}

dependencies {
    neoForge("net.neoforged:neoforge:${neoforgeVersion}")

    compileOnlyApi("mezz.jei:jei-${minecraftVersion}-common-api:${jeiVersion}")
    compileOnlyApi("mezz.jei:jei-${minecraftVersion}-neoforge-api:${jeiVersion}")
	// at runtime, use the full JEI jar for NeoForge
    runtimeOnly("mezz.jei:jei-${minecraftVersion}-neoforge:${jeiVersion}")

    implementation(project(":Common")) { isTransitive = false }
    shadowImplementation(project(":Common", configuration = "transformProductionNeoForge")) { isTransitive = false }

    implementation(project(":CommonApi")) { isTransitive = false }
    shadowImplementation(project(":CommonApi", configuration = "transformProductionNeoForge")) { isTransitive = false }
}

val apiJar = tasks.register<Jar>("apiJar") {
    from(project(":CommonApi").sourceSets.main.get().output)

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveClassifier.set("api")
}

artifacts {
    archives(apiJar.get())
    archives(tasks.shadowJar.get())
}

tasks.withType<Jar> {
    destinationDirectory.set(file(rootProject.rootDir.path + "/output"))
}

tasks.register<TaskPublishCurseForge>("publishCurseForge") {

    apiToken = System.getenv("CURSE_KEY") ?: "0"

    val mainFile = upload(curseProjectId, tasks.shadowJar.get())
    mainFile.changelogType = CFG_Constants.CHANGELOG_MARKDOWN
    mainFile.changelog = System.getenv("CHANGELOG") ?: ""
    mainFile.releaseType = CFG_Constants.RELEASE_TYPE_ALPHA
    mainFile.addModLoader("NeoForge")
    mainFile.addJavaVersion("Java $modJavaVersion")
    mainFile.addGameVersion(minecraftVersion)
    mainFile.addEnvironment("Client")
    //TODO: Figure out how to upload correct files
//    mainFile.withAdditionalFile(apiJar.get())
//    mainFile.withAdditionalFile(tasks.remapSourcesJar.get())
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN") ?: "0")
    projectId.set(modrinthProjectId)
    versionNumber.set("${project.version}")
    versionName.set("${project.version} for NeoForge $minecraftVersion")
    versionType.set("alpha")
    changelog.set(System.getenv("CHANGELOG") ?: "")
    uploadFile.set(tasks.shadowJar.get())
    gameVersions.add(minecraftVersion)
    // additionalFiles.addAll(arrayOf(apiJar.get(), sourcesJar.get())) // TODO: Figure out how to upload these
}
tasks.modrinth.get().dependsOn(tasks.shadowJar)