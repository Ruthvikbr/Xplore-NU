plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.parcelize")
//    id("com.google.protobuf")
}

android {
    namespace = "com.mobile.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

//    sourceSets { //This is the correction.
//        getByName("main") {
//            kotlin.srcDir("${layout.buildDirectory}/generated/source/proto/main/kotlin")
//        }
//    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(project(":domain"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.room.ktx)
    testImplementation(libs.androidx.room.testing)
    implementation(libs.androidx.room.paging)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(libs.retrofit)
    implementation(libs.converter.gson) // If you are using Gson for JSON conversion

    implementation(libs.logging.interceptor)
    implementation(libs.androidx.datastore.preferences)

//    implementation("androidx.datastore:datastore:1.0.0")
//    implementation("com.google.protobuf:protobuf-javalite:3.21.2")


}
//
//protobuf {
//    protoc {
//        artifact = "com.google.protobuf:protoc:3.21.2"
//    }
//    generateProtoTasks {
//        all().forEach { task ->
//            task.plugins.create("java") {
//                option("lite")
//            }
//        }
//    }
//}