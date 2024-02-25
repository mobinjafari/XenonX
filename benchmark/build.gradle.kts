plugins {
    id ("com.android.test")
    id ("org.jetbrains.kotlin.android")
}

android {
//    signingConfigs {
//        debug {
//            storePassword "k1l1d.c0m"
//            keyPassword "k1l1d.c0m"
//            storeFile file("C:\\DONOTDELETE\\kilid-sign\\Kilid-Sign")
//            keyAlias "Kilid"
//        }
//
//        release {
//            storePassword "k1l1d.c0m"
//            keyPassword "k1l1d.c0m"
//            storeFile file("C:\\DONOTDELETE\\kilid-sign\\Kilid-Sign")
//            keyAlias "Kilid"
//        }
//    }
    namespace  = "com.example.benchmark"
    compileSdk = 34

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose  = false
        buildConfig = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    defaultConfig {
        minSdk  = 24
        targetSdk  = 34

        testInstrumentationRunner  = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        // This benchmark buildType is used for benchmarking, and should function like your
        // release build (for example, with minification on). It"s signed with a debug key
        // for easy local/CI testing.
        create("benchmark") {
            buildConfigField("String", "BASE_URL", "\"http://185.208.79.114:8001/\"")
            buildConfigField("String", "UPLOAD_IMAGE_URL", "\"https://upload.kilid.com\"")
            buildConfigField("String", "ADVERTISEMENT_SALE_URL", "\"https://kilid.com/buy/detail/\"")
            buildConfigField("String", "ADVERTISEMENT_RENT_URL", "\"https://kilid.com/rent/detail/\"")
            buildConfigField("String", "CDN", "\"https://cdn.kilid.com/\"")
            isMinifyEnabled = false
            isShrinkResources = false
            matchingFallbacks += listOf("debug")
            proguardFiles("benchmark-rules.pro")
        }
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

dependencies {
    implementation ("androidx.test.ext:junit:1.1.5")
    implementation ("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("androidx.test.uiautomator:uiautomator:2.2.0")
    implementation ("androidx.benchmark:benchmark-macro-junit4:1.1.1")
    implementation ("androidx.profileinstaller:profileinstaller:1.3.1")
}

//androidComponents {
//    onVariants { variant ->
//        if (variant.buildType != "benchmark") {
//            // Iterate over and disable specific tasks related to the variant
//            val variantName = variant.name.capitalize()
//            val tasksToDisable = listOf(
//                "compile${variantName}Kotlin",
//                "assemble${variantName}",
//                // Add other task names as required
//            )
//            tasksToDisable.forEach { taskName ->
//                tasks.named(taskName).configure {
//                    enabled = false
//                }
//            }
//        }
//    }
//}



