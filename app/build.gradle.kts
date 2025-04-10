plugins {
    id("photomap.android.application.compose")
    id("photomap.android.application")
    id("photomap.android.hilt")
}

android {
    namespace = "com.example.photomap"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.navigation.compose)
    implementation(projects.feature.splash)
    implementation(projects.feature.home)

}