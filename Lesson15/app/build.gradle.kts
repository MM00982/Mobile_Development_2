plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "ru.mirea.musin.navigationapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "ru.mirea.musin.navigationapp"
        minSdk = 26 // Navigation Component требует минимум 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // ВКЛЮЧАЕМ VIEW BINDING
    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // NAVIGATION COMPONENT
    val nav_version = "2.8.4" // Или новее
    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:$nav_version")
}