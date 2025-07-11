plugins {
    id("photomap.android.feature")
    id("photomap.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.home"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.coil.compose)
    implementation(libs.material.icons.extended)
}