plugins {
    id("photomap.android.library")
    id("photomap.android.hilt")
}

android {
    namespace = "com.example.data"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.w3w)
    implementation(projects.core.network)
    implementation(projects.core.model)

}