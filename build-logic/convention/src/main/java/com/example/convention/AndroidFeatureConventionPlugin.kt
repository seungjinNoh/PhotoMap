import com.android.build.gradle.LibraryExtension
import com.example.convention.compose.configureAndroidCompose
import com.example.convention.compose.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                configureAndroidCompose(this)
                defaultConfig.targetSdk = 35
            }

            extensions.getByType<KotlinAndroidProjectExtension>().apply {
                configureKotlinAndroid(this)
            }

        }
    }
}