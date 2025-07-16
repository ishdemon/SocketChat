plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.ishdemon.chatapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ishdemon.chatapp"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "PIESOCKET_API_KEY", "\"htwbgMrb4KwXqUwfGvCk00oEExVM53ICTbhlUj1A\"")
        buildConfigField("String", "PIESOCKET_CLUSTER_ID", "\"s14932.blr1\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.gson)

    implementation(libs.room.runtime)
    implementation("androidx.room:room-ktx:2.6.1")
    kapt(libs.room.compiler)

    implementation(libs.lifecycle.viewmodel.ktx)
    implementation("com.piesocket:channels-sdk:1.0.5")
}