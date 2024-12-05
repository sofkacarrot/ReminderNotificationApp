plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.remindernotificationapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.remindernotificationapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation ("androidx.activity:activity-compose:1.7.2") // Для Compose
    implementation ("androidx.compose.ui:ui:1.5.1")             // Основной модуль Compose
    implementation ("androidx.compose.material:material:1.5.1") // Material Design для Compose
    implementation ("androidx.compose.ui:ui-tooling-preview:1.5.1") // Для предварительного просмотра
    implementation ("androidx.core:core-ktx:1.12.0")            // Библиотека KTX
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2") // Runtime для жизненного цикла
    implementation ("androidx.core:core:1.12.0")                // Core для NotificationCompat
    implementation ("androidx.core:core-ktx:1.12.0")
    implementation ("androidx.compose.material:material:1.5.1") //для уведомлений
    implementation ("com.google.accompanist:accompanist-permissions:0.33.1-alpha")


}