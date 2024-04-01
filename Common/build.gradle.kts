// gradle.properties
val platforms: String by extra
val minecraftVersion: String by extra
val jeiVersion: String by extra

architectury {
    common(platforms.split(","))
}

loom {
    accessWidenerPath.set(file("src/main/resources/jeresources.accesswidener"))
}

dependencies {
    api(project(":CommonApi", configuration = "namedElements"))
    compileOnly("mezz.jei:jei-${minecraftVersion}-common-api:${jeiVersion}")
}