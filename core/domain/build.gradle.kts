plugins {
    id("photomap.android.library")
    id("photomap.android.hilt")
}

android {
    namespace = "com.example.domain"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(projects.core.data)
    implementation(projects.core.model)
}