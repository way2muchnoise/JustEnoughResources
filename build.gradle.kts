// gradle.properties
val curseHomepageLink: String by extra
val curseProjectId: String by extra
val forgeVersion: String by extra
val forgeVersionRange: String by extra
var fabricVersion: String by extra
var fabricLoaderVersion: String by extra
val githubUrl: String by extra
val loaderVersionRange: String by extra
val mappingsChannel: String by extra
val mappingsVersion: String by extra
val minecraftVersion: String by extra
val minecraftVersionRange: String by extra
val modAuthor: String by extra
val modGroup: String by extra
val modId: String by extra
val modJavaVersion: String by extra
val modName: String by extra
val modFileName: String by extra
val specificationVersion: String by extra


subprojects {
    //adds the build number to the end of the version string if on a build server
    var buildNumber = System.getenv("TRAVIS_BUILD_NUMBER")
    if (buildNumber == null) {
        buildNumber = "local"
    }

    version = "${specificationVersion}.${buildNumber}"
    group = modGroup

    tasks.withType<Javadoc> {
        // workaround cast for https://github.com/gradle/gradle/issues/7038
        val standardJavadocDocletOptions = options as StandardJavadocDocletOptions
        // prevent java 8's strict doclint for javadocs from failing builds
        standardJavadocDocletOptions.addStringOption("Xdoclint:none", "-quiet")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(JavaLanguageVersion.of(modJavaVersion).asInt())
    }

    tasks.withType<Jar> {
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

    tasks.withType<ProcessResources> {
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
                "githubUrl" to githubUrl,
                "curseHomepageLink" to curseHomepageLink,
                "modAuthor" to modAuthor,
            ))
        }
    }

    repositories {
        // Sponge mirrors a lot of maven repos, using as fallback
        maven("https://repo.spongepowered.org/repository/maven-public/")
        // JEI repo before January 2023
        maven("https://dvs1.progwml6.com/files/maven/")
        // JEI repo after January 2023
        maven("https://maven.blamejared.com/")
        // JEI mirror
        maven("https://modmaven.dev")
    }
}