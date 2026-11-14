pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Pleos Playground SDK Repository (when configured locally or via HMG developer portal)
        flatDir {
            dirs("app/libs")
        }
    }
}
rootProject.name = "EV_MOMENT_Pleos"
include(":app")
