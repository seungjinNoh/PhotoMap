plugins {
    id("photomap.android.feature")
    id("photomap.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.map"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.foundation.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.maps.compose)
    implementation(libs.google.maps.sdk)
    implementation(libs.coil.compose)
    implementation(projects.core.domain)
    implementation(projects.core.utils)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)

}