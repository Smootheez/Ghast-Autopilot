plugins {
    id("fabric-conventions")
}

val fabricVersion: String by project
val modmenuVersion: String by project
val smoothiezApiVersion: String by project

repositories {
    mavenLocal()
    maven {
        name = "Terraformers"
        url = uri("https://maven.terraformersmc.com/")
    }
}

dependencies {
    // mod api dependencies
    modApi("net.fabricmc.fabric-api:fabric-api:${fabricVersion}") // already included in smoothiezapi, but still add for consistency
    modApi("io.github.smootheez:smoothiezapi:${smoothiezApiVersion}")

    // compile and runtime only dependencies
    modCompileOnly("com.terraformersmc:modmenu:${modmenuVersion}")
    modLocalRuntime("com.terraformersmc:modmenu:${modmenuVersion}")
}