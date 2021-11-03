plugins {
  id(Plugins.BuildPlugins.androidLib)
  id(Plugins.BuildPlugins.kotlinAndroid)
}

android {
  compileSdk = Sdk.compileSdk
  buildToolsVersion = Plugins.Versions.buildTools

  defaultConfig {
    minSdk = Sdk.minSdk
    targetSdk = Sdk.targetSdk
    testInstrumentationRunner = Dependencies.androidJunitRunner
    // Need to specify this to prevent junit runner from going deep into our dependencies
    testInstrumentationRunnerArguments["package"] = "com.google.android.fhir.workflow"
  }

  sourceSets { getByName("test").apply { resources.setSrcDirs(listOf("sampledata")) } }

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
  packagingOptions {
    exclude("license.html")
    exclude("META-INF/ASL-2.0.txt")
    exclude("META-INF/sun-jaxb.episode")
    exclude("META-INF/DEPENDENCIES")
    exclude("META-INF/LGPL-3.0.txt")
    exclude("readme.html")
  }
  kotlinOptions {
    // See https://developer.android.com/studio/write/java8-support
    jvmTarget = JavaVersion.VERSION_1_8.toString()
  }
  testOptions { unitTests.isIncludeAndroidResources = true }
  jacoco { version = "0.8.7" }
}

configurations {
  all {
    exclude(module = "json")
    exclude(module = "xpp3")
    exclude(module = "hamcrest-all")
    exclude(module = "jaxb-impl")
    exclude(module = "jaxb-core")
    exclude(module = "jakarta.activation-api")
    exclude(module = "javax.activation")
    exclude(module = "jakarta.xml.bind-api")
  }
}

dependencies {
  androidTestImplementation(Dependencies.AndroidxTest.core)
  androidTestImplementation(Dependencies.AndroidxTest.extJunitKtx)
  androidTestImplementation(Dependencies.AndroidxTest.runner)
  androidTestImplementation(Dependencies.AndroidxTest.workTestingRuntimeKtx)
  androidTestImplementation(Dependencies.junit)
  androidTestImplementation(Dependencies.truth)

  api(Dependencies.HapiFhir.structuresR4) { exclude(module = "junit") }

  coreLibraryDesugaring(Dependencies.desugarJdkLibs)

  implementation(Dependencies.Kotlin.androidxCoreKtx)
  implementation(Dependencies.Kotlin.kotlinCoroutinesAndroid)
  implementation(Dependencies.Kotlin.kotlinCoroutinesCore)
  implementation(Dependencies.Kotlin.stdlib)
  implementation("xerces:xercesImpl:2.11.0")
  implementation("com.github.java-json-tools:msg-simple:1.2")
  implementation("com.github.java-json-tools:jackson-coreutils:2.0")
  implementation("com.fasterxml.jackson.core:jackson-core:2.12.2")
  implementation("com.fasterxml.jackson.core:jackson-databind:2.12.2")
  implementation("com.fasterxml.jackson.core:jackson-annotations:2.12.2")
  implementation("org.codehaus.woodstox:woodstox-core-asl:4.4.1")
  implementation("javax.xml.stream:stax-api:1.0-2")
  implementation("org.opencds.cqf.cql:engine:1.5.2-SNAPSHOT")
  implementation("org.opencds.cqf.cql:engine.fhir:1.5.2-SNAPSHOT")
  implementation("org.opencds.cqf.cql:evaluator:1.2.1-SNAPSHOT")
  implementation("org.opencds.cqf.cql:evaluator.builder:1.2.1-SNAPSHOT")
  implementation("org.opencds.cqf.cql:evaluator.dagger:1.2.1-SNAPSHOT")
  implementation(project(":engine"))

  testImplementation(Dependencies.AndroidxTest.core)
  testImplementation(Dependencies.junit)
  testImplementation(Dependencies.robolectric)
  testImplementation(Dependencies.truth)
}
