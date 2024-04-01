// gradle.properties
val platforms: String by extra

architectury {
    common(platforms.split(","))
}

loom {
    accessWidenerPath.set(project(":Common").file("src/main/resources/jeresources.accesswidener"))
}

dependencies {
    // None
}