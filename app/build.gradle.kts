plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.parcelize")
}

android {
    namespace = "com.mobile.xplore_nu"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mobile.xplore_nu"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
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

    implementation("androidx.test.ext:junit:1.1.5") // ADDED, BEFORE other dependencies

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(libs.androidx.ui.test.junit4.android)
    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit) // Uses libs object, correct version
    androidTestImplementation(libs.androidx.espresso.core) // Uses libs object, correct version

    // Compose UI test dependencies (consolidated)
    androidTestImplementation(libs.androidx.ui.test.junit4) // Uses libs object
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    androidTestImplementation(platform(libs.androidx.compose.bom)) // Keep this
    debugImplementation(libs.androidx.ui.tooling) // Keep this
    debugImplementation(libs.androidx.ui.test.manifest) // Keep this

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    testImplementation(libs.mockk)

    //mapbox
    implementation(libs.mapbox.android) {
        exclude(group = "com.google.android.gms", module = "play-services-cronet")
    }
    implementation("com.mapbox.navigationcore:android:3.8.1")
    implementation("com.mapbox.navigationcore:navigation:3.8.1")
    implementation("com.mapbox.navigationcore:ui-maps:3.8.1")
    implementation("com.mapbox.navigationcore:voice:3.8.1")
    implementation("com.mapbox.navigationcore:ui-components:3.8.1")

    implementation(libs.maps.compose)
    implementation(libs.play.services.location)
    implementation(libs.kotlin.coroutines.play)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.otpverification)

    implementation(libs.androidx.material)
    implementation(libs.accompanist.permissions)

    implementation(libs.compose)

    debugImplementation(libs.ui.tooling) // Keep this
    debugImplementation(libs.ui.test.manifest) // Keep this
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.7")

    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)

}