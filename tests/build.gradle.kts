import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-android")
}

android {
    namespace = "com.starshas.themoviedb.tests"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        buildConfig = true
    }

    val secretsPropertiesFile = project.file("tests.secrets.properties")
    val secretsProperties = Properties()

    if (secretsPropertiesFile.exists()) {
        secretsProperties.load(secretsPropertiesFile.inputStream())
    } else {
        throw GradleException("tests.secrets.properties could not be found.")
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "TEST_API_KEY", "${secretsProperties["TEST_API_KEY"]}")
        }
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
    implementation(project(":data"))
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
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
    testImplementation("io.mockk:mockk:1.12.5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
