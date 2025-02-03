import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-android")
}

android {
    namespace = "com.starshas.themoviedb"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.starshas.themoviedb"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        buildConfig = true
    }

    val secretsPropertiesFile = project.rootProject.file("secrets.properties")
    val secretsProperties = Properties()

    if (secretsPropertiesFile.exists()) {
        secretsProperties.load(secretsPropertiesFile.inputStream())
    } else {
        throw GradleException("secrets.properties could not be found.")
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "API_KEY", "${secretsProperties["API_KEY"]}")
        }
        getByName("release") {
            buildConfigField("String", "API_KEY", "${secretsProperties["API_KEY"]}")
        }
    }

    viewBinding {
        enable = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
}

dependencies {
    implementation(project(":di"))
    implementation(project(":domain"))
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.48")
    implementation("com.google.dagger:hilt-android:2.48")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    kapt("androidx.hilt:hilt-compiler:1.1.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.12.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
