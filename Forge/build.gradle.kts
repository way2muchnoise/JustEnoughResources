import net.darkhax.curseforgegradle.TaskPublishCurseForge
import net.darkhax.curseforgegradle.Constants as CFG_Constants

plugins {
	id("java")
	id("idea")
	id("maven-publish")
	id("net.minecraftforge.gradle") version("[6.0,6.2)")
	id("org.parchmentmc.librarian.forgegradle") version("1.+")
	id("net.darkhax.curseforgegradle") version("1.0.11")
	id("com.modrinth.minotaur") version("2.+")
}

// gradle.properties
val curseHomepageLink: String by extra
val curseProjectId: String by extra
val modrinthProjectId: String by extra
val forgeVersion: String by extra
val mappingsChannel: String by extra
val mappingsParchmentMinecraftVersion: String by extra
val mappingsParchmentVersion: String by extra
val minecraftVersion: String by extra
val jeiVersion: String by extra
val modId: String by extra
val modFileName: String by extra
val modJavaVersion: String by extra

val baseArchivesName = "${modFileName}-${minecraftVersion}"
base {
	archivesName.set(baseArchivesName)
}

sourceSets {
}

val dependencyProjects: List<ProjectDependency> = listOf(
	project.dependencies.project(":Common"),
	project.dependencies.project(":CommonApi"),
	project.dependencies.project(":ForgeApi"),
)

dependencyProjects.forEach {
	project.evaluationDependsOn(it.dependencyProject.path)
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
	compileOnly(fg.deobf("mezz.jei:jei-${minecraftVersion}-forge-api:${jeiVersion}"))
	// at runtime, use the full JEI jar for Forge
	runtimeOnly(fg.deobf("mezz.jei:jei-${minecraftVersion}-forge:${jeiVersion}"))
	dependencyProjects.forEach {
		implementation(it)
	}
}

minecraft {
	mappings(mappingsChannel, "${mappingsParchmentMinecraftVersion}-${mappingsParchmentVersion}-${minecraftVersion}")
	// mappings("official", minecraftVersion)

	copyIdeResources.set(true)

	accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

	runs {
		val client = create("client") {
			taskName("runClientDev")
			property("forge.logging.console.level", "debug")
			workingDirectory(file("run/client/Dev"))
			mods {
				create(modId) {
					source(sourceSets.main.get())
					for (p in dependencyProjects) {
						source(p.dependencyProject.sourceSets.main.get())
					}
				}
			}
		}
		create("client_01") {
			taskName("runClientPlayer01")
			parent(client)
			workingDirectory(file("run/client/Player01"))
			args("--username", "Player01")
		}
		create("client_02") {
			taskName("runClientPlayer02")
			parent(client)
			workingDirectory(file("run/client/Player02"))
			args("--username", "Player02")
		}
		create("server") {
			taskName("Server")
			property("forge.logging.console.level", "debug")
			workingDirectory(file("run/server"))
			mods {
				create(modId) {
					source(sourceSets.main.get())
					for (p in dependencyProjects) {
						source(p.dependencyProject.sourceSets.main.get())
					}
				}
			}
		}
	}
}

tasks.named<Jar>("jar") {
	from(sourceSets.main.get().output)
	for (p in dependencyProjects) {
		from(p.dependencyProject.sourceSets.main.get().output)
	}

	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	finalizedBy("reobfJar")
}

val apiJar = tasks.register<Jar>("apiJar") {
	from(project(":CommonApi").sourceSets.main.get().output)
	from(project(":ForgeApi").sourceSets.main.get().output)

	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	finalizedBy("reobfJar")
	archiveClassifier.set("api")
}

val sourcesJar = tasks.register<Jar>("sourcesJar") {
	from(sourceSets.main.get().allJava)
	for (p in dependencyProjects) {
		from(p.dependencyProject.sourceSets.main.get().allJava)
	}

	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	finalizedBy("reobfJar")
	archiveClassifier.set("sources")
}

tasks.register<TaskPublishCurseForge>("publishCurseForge") {

	apiToken = System.getenv("CURSE_KEY") ?: "0"

	val mainFile = upload(curseProjectId, tasks.jar.get())
	mainFile.changelogType = CFG_Constants.CHANGELOG_MARKDOWN
	mainFile.changelog = System.getenv("CHANGELOG") ?: ""
	mainFile.releaseType = CFG_Constants.RELEASE_TYPE_ALPHA
	mainFile.addModLoader("Forge")
	mainFile.addJavaVersion("Java $modJavaVersion")
	mainFile.addGameVersion(minecraftVersion)
	mainFile.withAdditionalFile(apiJar.get())
	mainFile.withAdditionalFile(sourcesJar.get())
}

modrinth {
	token.set(System.getenv("MODRINTH_TOKEN") ?: "0")
	projectId.set(modrinthProjectId)
	versionNumber.set("${project.version}")
	versionName.set("${project.version} for Forge $minecraftVersion")
	versionType.set("alpha")
	uploadFile.set(tasks.jar.get())
	gameVersions.add(minecraftVersion)
	// additionalFiles.addAll(arrayOf(apiJar.get(), sourcesJar.get())) // TODO: Figure out how to upload these
	dependencies {
		required.project("jei")
	}
}
tasks.modrinth.get().dependsOn(tasks.jar)

artifacts {
	archives(apiJar.get())
	archives(sourcesJar.get())
	archives(tasks.jar.get())
}

tasks.withType<Jar> {
	destinationDirectory.set(file(rootProject.rootDir.path + "/output"))
}

publishing {
	publications {
		register<MavenPublication>("maven") {
			artifactId = baseArchivesName
			artifact(apiJar.get())
			artifact(sourcesJar.get())
			artifact(tasks.jar.get())
		}
	}
	repositories {
		val deployDir = project.findProperty("DEPLOY_DIR")
		if (deployDir != null) {
			maven(deployDir)
		}
	}
}

idea {
	module {
		for (fileName in listOf("run", "out", "logs")) {
			excludeDirs.add(file(fileName))
		}
	}
}