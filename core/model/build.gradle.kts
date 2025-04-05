plugins {
    id("photomap.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.model"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

}