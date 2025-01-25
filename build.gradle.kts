// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        set("lifecycle_version", "2.6.2")
    }
}

plugins {
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false

    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}