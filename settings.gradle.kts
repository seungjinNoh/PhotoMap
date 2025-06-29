pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        kotlin("jvm") version "2.0.20"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))

rootProject.name = "PhotoMap"
include(":app")
include(":feature")
include(":feature:splash")
include(":core")
include(":core")
include(":core:designsystem")
include(":feature:home")
include(":core:domain")
include(":core:data")
include(":core:network")
include(":core:model")
include(":core:navigation")
include(":feature:map")
include(":feature:edit")
include(":core:utils")
include(":feature:search")
