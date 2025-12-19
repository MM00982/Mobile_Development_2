// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    dependencies {
        // Проверь версию, можно 4.4.1 или новее
        classpath("com.google.gms:google-services:4.4.1")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
}

