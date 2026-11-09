// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) version "8.7.2" apply false
    alias(libs.plugins.kotlin.android) version "2.0.21" apply false
    alias(libs.plugins.kotlin.compose) version "2.0.21" apply false
    alias(libs.plugins.ksp) version "2.0.21-1.0.27" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}