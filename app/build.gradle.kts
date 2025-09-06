plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.assignment1c030RajKothari"
    compileSdk = 36
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }


    defaultConfig {
        applicationId = "com.example.assignment1c030"
        minSdk = 36
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val apiKey: String = project.findProperty("GEMINI_API_KEY") as String? ?: "AIzaSyALkJFN3GH_QgEfVVPEEf28swhejfF71xc"
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
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
}

dependencies {

    implementation(libs.appcompat)
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.cardview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}