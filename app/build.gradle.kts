plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    namespace = "com.mendelin.square"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mendelin.square"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.mendelin.square.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            /* Keep local server for DEBUG build type */
            buildConfigField("String", "BASE_URL", "\"https://api.github.com/\"")
        }

        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            /* Keep production server for RELEASE build type */
            buildConfigField("String", "BASE_URL", "\"https://api.github.com/\"")
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1,LICENSE*.md}"
        }
    }

    testOptions {
        packaging {
            jniLibs {
                useLegacyPackaging = true
            }
        }
        animationsDisabled = true
    }

    hilt {
        enableAggregatingTask = true
    }

    kapt {
        useBuildCache = true
        correctErrorTypes = true
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
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")
    implementation("androidx.compose.material:material-icons-extended-android:1.6.8")
    implementation("androidx.paging:paging-compose:3.3.0")

    implementation("androidx.compose.material3.adaptive:adaptive:1.0.0-beta04")
    implementation("androidx.compose.material3.adaptive:adaptive-layout:1.0.0-beta04")
    implementation("androidx.compose.material3.adaptive:adaptive-navigation:1.0.0-beta04")

    testImplementation(libs.junit)
    testImplementation("com.google.truth:truth:1.4.4")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    testImplementation("io.mockk:mockk-android:1.13.12")
    testImplementation("io.mockk:mockk-agent:1.13.12")
    testImplementation("com.google.dagger:hilt-android-testing:2.51.1")
    kaptTest("com.google.dagger:hilt-android-compiler:2.51.1")

    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation("androidx.compose.ui:ui-test:1.6.8")
    androidTestImplementation("com.google.truth:truth:1.4.4")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.51.1")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    androidTestImplementation("app.cash.turbine:turbine:1.1.0")
    androidTestImplementation("io.mockk:mockk-android:1.13.12")
    androidTestImplementation("io.mockk:mockk-agent:1.13.12")
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test:monitor:1.7.1")
    androidTestImplementation("androidx.test.services:test-services:1.5.0")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.51.1")

    debugImplementation("androidx.tracing:tracing:1.2.0")
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    /* Gson - JSON */
    implementation("com.google.code.gson:gson:2.11.0")

    /* Retrofit - networking */
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    /* Hilt - dependency injection */
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")

    /* Timber - logging */
    implementation("com.jakewharton.timber:timber:5.0.1")

    /* Coil - image loading */
    implementation("io.coil-kt:coil-compose:2.7.0")
}
