import java.util.Properties

plugins {
    id("photomap.android.library")
}

android {
    namespace = "com.example.data"
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.w3w)
}