val mavenGroup: String by project

allprojects {
    group = mavenGroup
}

subprojects {
    afterEvaluate {
        // Only configure if fabric-loom plugin is applied
        plugins.findPlugin("fabric-loom")?.let {
            tasks.named("remapJar") {
                (this as AbstractArchiveTask).archiveBaseName.set(rootProject.name)
            }

            tasks.named("remapSourcesJar") {
                (this as AbstractArchiveTask).archiveBaseName.set(rootProject.name)
            }
        }
    }
}