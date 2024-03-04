plugins {
    id ("com.android.application")
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
    id ("org.jetbrains.kotlin.plugin.serialization")
    id ("org.jetbrains.kotlin.android")
    id ("kotlin-parcelize")
   // id("com.google.protobuf") version "0.9.4"
    id ("com.google.firebase.crashlytics")
//    id ("com.google.firebase.firebase-perf")
    id ("com.google.gms.google-services")
}

android {
    compileSdk =  34
    defaultConfig {
        applicationId = "org.lotka.xenonx"
        minSdk =  21
        targetSdk = 34
        versionCode = 164
        versionName =   "6.0.5"
        multiDexEnabled =  true

        testInstrumentationRunner =  "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            keyAlias = "Kilid"
            keyPassword = "k1l1d.c0m"
            storeFile = file("portalgoogleplaysign.jks")
            storePassword = "k1l1d.c0m"
        }
    }

    buildTypes {
        namespace = "org.lotka.xenonx"
        getByName("debug")  {
            buildConfigField("String", "BASE_URL", "\"http://185.208.79.114:8001/\"")
            buildConfigField("String", "UPLOAD_IMAGE_URL", "\"https://upload.kilid.com\"")
            buildConfigField("String", "ADVERTISEMENT_SALE_URL", "\"https://kilid.com/buy/detail/\"")
            buildConfigField("String", "ADVERTISEMENT_RENT_URL", "\"https://kilid.com/rent/detail/\"")
            buildConfigField("String", "CDN", "\"https://cdn.kilid.com/\"")
            //turn on for release
            isMinifyEnabled =  false
            isShrinkResources = false
            isDebuggable=  true
            isJniDebuggable = true
        }


        getByName("release") {
            buildConfigField("String", "BASE_URL", "\"https://server.kilid.com/\"")
            buildConfigField("String", "UPLOAD_IMAGE_URL", "\"https://upload.kilid.com\"")
            buildConfigField("String", "ADVERTISEMENT_SALE_URL", "\"https://kilid.com/buy/detail/\"")
            buildConfigField("String", "ADVERTISEMENT_RENT_URL", "\"https://kilid.com/rent/detail/\"")
            buildConfigField("String", "CDN", "\"https://cdn.kilid.com/\"")
            //turn on for release
            isMinifyEnabled =  true
            isShrinkResources = true
            isDebuggable=  false
            isJniDebuggable = false
            proguardFiles( getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }


    compileOptions {
        sourceCompatibility  = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose  = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion  = "1.5.8"
        kotlinCompilerVersion  = "1.5.8"
    }
    packagingOptions {
        resources {
            exclude("/META-INF/{AL2.0,LGPL2.1}")
            exclude ("META-INF/gradle/incremental.annotation.processors")
        }
    }

}

// Define a task to print the version code
tasks.register("printVersionCode") {
    doLast {
        println(android.defaultConfig.versionCode)
    }
}

// Define a task to print the version name
tasks.register("printVersionName") {
    doLast {
        println(android.defaultConfig.versionName)
    }
}

dependencies {

    implementation ("org.jetbrains.kotlin:kotlin-reflect:1.9.22")
//    implementation("com.google.protobuf:protobuf-java:3.19.3")



    // Navigation
    implementation ("androidx.navigation:navigation-compose:2.4.0-alpha10")
    implementation ("com.google.accompanist:accompanist-navigation-animation:0.22.0")

    implementation  ("com.onesignal:OneSignal:[3.15.2, 3.99.99]")



    implementation ("androidx.core:core-ktx:1.12.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.10.0")
    implementation("androidx.autofill:autofill:1.1.0")

    implementation ("androidx.compose.ui:ui:1.5.3")
    implementation ("androidx.compose.material:material:1.5.4")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.5.3")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation ("androidx.activity:activity-compose:1.8.0")

    // ConstraintLayout
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Coil image library
    implementation ("io.coil-kt:coil-compose:2.2.2")


    // ImagePicker - single selector
    implementation ("com.github.dhaval2404:imagepicker:2.1")

    //Image compressor
    implementation ("com.github.hkk595:Resizer:v1.5")

    // Android MPChart
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // DatePicker
    implementation ("com.github.aliab:Persian-Date-Picker-Dialog:1.7.1")

    // Date
    implementation ("com.github.samanzamani:PersianDate:1.4.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

    // Networking
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")
    implementation ("com.squareup.okhttp3:okhttp:4.11.0")

    // PlayService
    implementation ("com.google.android.gms:play-services-auth-api-phone:18.0.1")
    //Phone number validation
    implementation ("com.googlecode.libphonenumber:libphonenumber:8.13.5")

    // Room
    implementation ("androidx.room:room-runtime:2.6.0")
    implementation ("androidx.core:core-ktx:1.12.0")
    implementation ("com.google.firebase:firebase-messaging-ktx:23.3.1")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
    implementation("com.google.firebase:firebase-crashlytics-ktx:18.6.2")
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.2")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("androidx.compose.material3:material3-android:1.2.0")
    implementation("com.google.ai.client.generativeai:generativeai:0.2.1")
    kapt ("androidx.room:room-compiler:2.6.0")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.6.0")
    annotationProcessor ("androidx.room:room-compiler:2.6.0")

    // Timber
    implementation ("com.jakewharton.timber:timber:5.0.1")

    // DataStore
    implementation ("androidx.datastore:datastore-preferences:1.0.0")

    // Proto Datastore
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
//    implementation ("com.google.protobuf:protobuf-javalite:3.21.11")
    implementation ("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")

    // Navigation Compose
    implementation ("androidx.navigation:navigation-compose:2.7.5")

    // Hilt
    implementation ("com.google.dagger:hilt-android:2.46.1")
    kapt ("com.google.dagger:hilt-compiler:2.46.1")
    implementation ("androidx.hilt:hilt-navigation-compose:1.1.0")

    // Accompanist Pager
    implementation ("com.google.accompanist:accompanist-pager:0.30.1")
    // If using indicators, also depend on
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.28.0")

    // Map
//    implementation ("ir.map:map-sdk:5.0.3")

    // Digits to persian letter
    implementation ("com.github.yamin8000:PrettyPersianNumbers:1.1.1")


    // Android MP Chart
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Crash reporter: Appmetrica
    implementation ("com.yandex.android:mobmetricalib:5.2.0")

    implementation ("com.google.accompanist:accompanist-insets:0.28.0")
    implementation ("com.google.accompanist:accompanist-permissions:0.30.1")
    implementation ("com.google.accompanist:accompanist-flowlayout:0.30.1")

    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.5.3")
    debugImplementation ("androidx.compose.ui:ui-tooling:1.5.3")


    implementation ("com.facebook.soloader:soloader:0.10.5")

    // pull to refresh
    implementation ("com.google.accompanist:accompanist-swiperefresh:0.30.1")
    implementation("androidx.biometric:biometric:1.2.0-alpha05")
    // time and date
    implementation ("com.jakewharton.threetenabp:threetenabp:1.4.0")


    // image slider
    implementation ("com.google.accompanist:accompanist-pager:0.30.1")
    implementation ("com.airbnb.android:lottie-compose:6.0.1")

    //gms - auth APIs
    implementation ("com.google.android.gms:play-services-base:18.2.0")
    implementation ("com.google.android.gms:play-services-auth-api-phone:18.0.1")
    //Optional for phone number hint
    implementation ("com.google.android.gms:play-services-auth:20.7.0")


    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.30.1")
    implementation ("com.google.accompanist:accompanist-swiperefresh:0.30.1")


    //firebase

    implementation ("com.google.firebase:firebase-analytics-ktx:21.5.0")
    implementation ("com.google.firebase:firebase-analytics:21.5.0")


//    implementation ("com.google.firebase:firebase-crashlytics-ktx:18.5.1")
//    implementation ("com.google.firebase:firebase-perf-ktx:20.5.0")
    implementation ("com.google.android.gms:play-services-tagmanager:18.0.4")
    implementation ("com.google.android.gms:play-services-tagmanager-v4-impl:18.0.4")

    implementation ("androidx.core:core-animation:1.0.0-rc01")
    //mpandroid chart
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
}
