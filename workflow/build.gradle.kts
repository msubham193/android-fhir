plugins {
    id(Plugins.BuildPlugins.androidLib)
    id(Plugins.BuildPlugins.kotlinAndroid)
}

android {
    compileSdkVersion(Sdk.compileSdk)
    buildToolsVersion(Plugins.Versions.buildTools)

    defaultConfig {
        minSdkVersion(Sdk.minSdk)
        targetSdkVersion(Sdk.targetSdk)
        testInstrumentationRunner(Dependencies.androidJunitRunner)
        // Need to specify this to prevent junit runner from going deep into our dependencies
        testInstrumentationRunnerArguments(mapOf("package" to "com.google.android.fhir.workflow"))
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
        getByName("debug") { isTestCoverageEnabled = true }
    }
    compileOptions {
        // Flag to enable support for the new language APIs
        // See https://developer.android.com/studio/write/java8-support
        isCoreLibraryDesugaringEnabled = true
        // Sets Java compatibility to Java 8
        // See https://developer.android.com/studio/write/java8-support
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        // See https://developer.android.com/studio/write/java8-support
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    testOptions { unitTests.isIncludeAndroidResources = true }
}

dependencies {
    androidTestImplementation(Dependencies.AndroidxTest.extJunit)
    androidTestImplementation(Dependencies.AndroidxTest.extJunitKtx)
    androidTestImplementation(Dependencies.AndroidxTest.runner)

    coreLibraryDesugaring(Dependencies.desugarJdkLibs)

    implementation(Dependencies.Kotlin.androidxCoreKtx)
    implementation(Dependencies.Kotlin.stdlib)

    testImplementation(Dependencies.junit)
}