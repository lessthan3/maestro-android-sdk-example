import java.util.Properties
import kotlin.apply

pluginManagement {
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
}

val localProps: Properties by lazy {
    Properties().apply {
        val f = File(rootDir, "local.properties")
        if (f.exists()) {
            f.inputStream().use { load(it) }
        }
    }
}

fun Properties.getOptional(name: String): String? = getProperty(name)

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        maven("https://jitpack.io")
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/lessthan3/MaestroKit.android")
            credentials {
                username = localProps.getOptional("githubUsername")
                password = localProps.getOptional("githubPass")
            }
        }
    }
}

rootProject.name = "My Application"
include(":app")
 