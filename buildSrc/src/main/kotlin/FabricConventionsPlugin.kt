import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.gradle.language.jvm.tasks.ProcessResources

class FabricConventionsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            // Apply plugins
            pluginManager.apply("java")
            pluginManager.apply("fabric-loom")

            // Get properties
            val modVersion: String by project
            val minecraftVersion: String by project
            val loaderVersion: String by project
            val targetJavaVersion: String by project
            val modid: String by project

            version = "${modVersion}+${minecraftVersion}"

            // Configure Loom
            extensions.configure<LoomGradleExtensionAPI>("loom") {
                splitEnvironmentSourceSets()
                mods {
                    create(modid) {
                        sourceSet(project.extensions.getByType(SourceSetContainer::class.java).getByName("main"))
                        sourceSet(project.extensions.getByType(SourceSetContainer::class.java).getByName("client"))
                    }
                }
            }

            // Configure dependencies
            dependencies {
                add("minecraft", "com.mojang:minecraft:${minecraftVersion}")
                add(
                    "mappings",
                    project.extensions.getByType(LoomGradleExtensionAPI::class.java).officialMojangMappings()
                )
                add("modApi", "net.fabricmc:fabric-loader:${loaderVersion}")

                add("testImplementation", platform("org.junit:junit-bom:5.10.0"))
                add("testImplementation", "org.junit.jupiter:junit-jupiter")
                add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")
            }

            // Configure test task
            tasks.named("test") {
                (this as org.gradle.api.tasks.testing.Test).useJUnitPlatform()
            }

            // Configure processResources
            tasks.named("processResources", ProcessResources::class.java) {
                val safeProps = project.properties
                    .filterValues { it is String || it is Number || it is Boolean }
                inputs.properties(safeProps)
                filteringCharset = "UTF-8"
                filesMatching("fabric.mod.json") {
                    expand(safeProps)
                }
            }

            // Configure Java compilation
            tasks.withType<JavaCompile>().configureEach {
                options.encoding = "UTF-8"
                val targetVersion = targetJavaVersion.toIntOrNull() ?: 8
                if (targetVersion >= 10 || JavaVersion.current().isJava10Compatible) {
                    options.release.set(targetVersion)
                }
            }

            // Configure Java extension
            extensions.configure<JavaPluginExtension>("java") {
                val javaVersion = JavaVersion.toVersion(targetJavaVersion)
                if (JavaVersion.current() < javaVersion) {
                    toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion.toInt()))
                }
                withSourcesJar()
            }
        }
    }
}