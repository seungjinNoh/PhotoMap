import java.util.Properties

plugins {
    id("photomap.android.library")
    id("photomap.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.network"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        val w3wApiKey = project.rootProject.file("local.properties").inputStream().use {
            Properties().apply { load(it) }
        }.getProperty("W3W_API_KEY") ?: ""

        buildConfigField("String", "W3W_API_KEY", "\"$w3wApiKey\"")
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.w3w)
}