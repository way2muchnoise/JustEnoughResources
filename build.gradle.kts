import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.task.RemapJarTask

plugins {
    base
    id("architectury-plugin") version ("3.4-SNAPSHOT")
    id("dev.architectury.loom") version ("1.5-SNAPSHOT") apply (false)
    id("com.github.johnrengelman.shadow") version ("7.1.2") apply (false)
}

// gradle.properties
val curseHomepageLink: String by extra
val curseProjectId: String by extra
val forgeVersion: String by extra
val forgeVersionRange: String by extra
val loaderVersionRange: String by extra
val neoforgeVersion: String by extra
val neoforgeVersionRange: String by extra
val neoforgeLoaderVersionRange: String by extra
val githubUrl: String by extra
val mappingsChannel: String by extra
val mappingsVersion: String by extra
val mappingsParchmentMinecraftVersion: String by extra
val mappingsParchmentVersion: String by extra
val minecraftVersion: String by extra
val minecraftVersionRange: String by extra
val modAuthor: String by extra
val modGroup: String by extra
val modId: String by extra
val modJavaVersion: String by extra
val modName: String by extra
val modFileName: String by extra
val specificationVersion: String by extra

// Set the Minecraft version for Architectury.
architectury {
    minecraft = minecraftVersion
}

//adds the build number to the end of the version string if on a build server
var buildNumber = System.getenv("TRAVIS_BUILD_NUMBER")
if (buildNumber == null) {
    buildNumber = "local"
}

version = "${specificationVersion}.${buildNumber}"
group = modGroup
base {
    archivesName.set(modFileName)
}


subprojects {
    apply(plugin = "java")
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "architectury-plugin")

    version = rootProject.version

    // Set Java version.
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    architectury {
        compileOnly()
    }

    // Find the loom extension. Since it's not applied to the root project, we can't access it directly by name in this file.
    val loom = project.extensions.getByName<LoomGradleExtensionAPI>("loom")

    loom.silentMojangMappingsLicense()

    dependencies {
        "minecraft"("com.mojang:minecraft:${minecraftVersion}")
        // Parchement mappings
        "mappings"(loom.layered {
            officialMojangMappings()
            parchment("org.parchmentmc.data:parchment-${mappingsParchmentMinecraftVersion}:${mappingsParchmentVersion}@zip")
        })
    }

    repositories {
        maven("https://maven.parchmentmc.org/")
        maven("https://maven.terraformersmc.com/releases/")
        maven("https://maven.shedaniel.me/")
        maven("https://maven.blamejared.com/")
    }

    tasks {
        withType<RemapJarTask> {
            val now = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(java.util.Date())
            manifest {
                attributes(mapOf(
                        "Specification-Title" to modName,
                        "Specification-Vendor" to modAuthor,
                        "Specification-Version" to specificationVersion,
                        "Implementation-Title" to name,
                        "Implementation-Version" to archiveVersion,
                        "Implementation-Vendor" to modAuthor,
                        "Implementation-Timestamp" to now,
                ))
            }
        }

        withType<Javadoc> {
            // workaround cast for https://github.com/gradle/gradle/issues/7038
            val standardJavadocDocletOptions = options as StandardJavadocDocletOptions
            // prevent java 8's strict doclint for javadocs from failing builds
            standardJavadocDocletOptions.addStringOption("Xdoclint:none", "-quiet")
        }

        withType<JavaCompile> {
            options.encoding = "UTF-8"
            options.release.set(JavaLanguageVersion.of(modJavaVersion).asInt())
        }

        withType<ProcessResources> {
            // this will ensure that this task is redone when the versions change.
            inputs.property("version", version)

            filesMatching(listOf("META-INF/mods.toml", "pack.mcmeta", "fabric.mod.json")) {
                expand(mapOf(
                        "modId" to modId,
                        "modName" to modName,
                        "version" to version,
                        "minecraftVersionRange" to minecraftVersionRange,
                        "forgeVersionRange" to forgeVersionRange,
                        "loaderVersionRange" to loaderVersionRange,
                        "neoforgeVersionRange" to neoforgeVersionRange,
                        "neoforgeLoaderVersionRange" to neoforgeLoaderVersionRange,
                        "githubUrl" to githubUrl,
                        "curseHomepageLink" to curseHomepageLink,
                        "modAuthor" to modAuthor,
                ))
            }
        }

        // Make all archives reproducible.
        withType<AbstractArchiveTask> {
            isReproducibleFileOrder = true
            isPreserveFileTimestamps = false
        }

        named<RemapJarTask>("remapJar") {
            archiveClassifier.set(null as String?)
        }

        withType<Jar> {
            archiveClassifier.set("dev")
        }
    }
}

// Set up "platform" subprojects (non-common subprojects).
subprojects {
    if (path != ":Common" && path != ":CommonApi") {
        // Apply the shadow plugin which lets us include contents of any libraries in our mod jars.
        // Architectury uses it for bundling the common mod code in the platform jars.
        apply(plugin = "com.github.johnrengelman.shadow")

        // Set a different run directory for the server run config,
        // so it won't override client logs/config (or vice versa).
        extensions.configure<LoomGradleExtensionAPI> {
            runConfigs.getByName("server") {
                runDir = "run/server"
            }

            // "main" matches the default Forge & NeoForge mod's name
            with(mods.maybeCreate("main")) {
                fun Project.sourceSets() = extensions.getByName<SourceSetContainer>("sourceSets")
                sourceSet(sourceSets().getByName("main"))
                sourceSet(project(":Common").sourceSets().getByName("main"))
            }
        }

        val shadowImplementation by configurations.creating {
            isCanBeConsumed = false
            isCanBeResolved = true
        }

        tasks {
            "shadowJar"(ShadowJar::class) {
                archiveClassifier.set("dev-shadow")
                // Load only our own shadow task
                configurations = listOf(shadowImplementation)
                exclude("architectury.common.json")
            }

            "remapJar"(RemapJarTask::class) {
                dependsOn("shadowJar")
                // Replace the remap jar task's input with the shadow jar containing the common classes.
                inputFile.set(named<ShadowJar>("shadowJar").flatMap { it.archiveFile })
            }
        }
    }
}

