plugins {
    id("photomap.android.library")
    id("photomap.android.hilt")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.data"
}

configurations.all {
    exclude(group = "com.intellij", module = "annotations")
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
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    implementation(libs.room.compiler)
    ksp(libs.room.compiler)

}