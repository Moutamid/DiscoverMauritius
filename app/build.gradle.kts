@file:Suppress("DEPRECATION")

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")

    id("kotlin-android")


}

android {
    namespace = "com.moutamid.sqlapp"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.moutamid.sqlapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 8
        versionName = "1.0.5"
        multiDexEnabled = true
        setProperty("archivesBaseName", "Discover Mauritius-$versionName")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    packagingOptions {
        exclude("META-INF/NOTICE.md")
        exclude("META-INF/NOTICE")
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/LICENSE")
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(project(":android-pdf-viewer"))
    testImplementation("junit:junit:4.13.2")
    implementation("com.github.ibrahimsn98:SmoothBottomBar:1.7.9")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    implementation("com.fxn769:stash:1.3.2")
    implementation("com.github.akshay2211:Stash:1c45b0e5d5")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.sun.mail:android-mail:1.6.7")
    implementation("com.sun.mail:android-activation:1.6.7")
    implementation("io.github.chochanaresh:filepicker:0.1.9")
    implementation(platform("com.google.firebase:firebase-bom:32.2.3"))
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation("joda-time:joda-time:2.10.10")
    implementation("org.greenrobot:eventbus:3.1.1")
//    implementation("com.gjiazhe:scrollparallaximageview:1.0")
    implementation("com.github.gjiazhe:ScrollParallaxImageView:83d063fe2e")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.20")
    implementation("org.mapsforge:mapsforge-core:0.18.0")
    implementation("org.mapsforge:mapsforge-map:0.18.0")
    implementation("org.mapsforge:mapsforge-map-reader:0.18.0")
    implementation("org.mapsforge:mapsforge-themes:0.18.0")
    implementation("net.sf.kxml:kxml2:2.3.0")
    implementation("org.mapsforge:mapsforge-map-android:0.18.0")
    implementation("com.caverock:androidsvg:1.4")
    implementation("org.mapsforge:mapsforge-poi:0.18.0")
    implementation("org.mapsforge:mapsforge-poi-android:0.18.0")
    implementation("org.mapsforge:sqlite-android:0.18.0")
    implementation("org.mapsforge:sqlite-android:0.18.0:natives-armeabi-v7a")
    implementation("org.mapsforge:sqlite-android:0.18.0:natives-arm64-v8a")
    implementation("org.mapsforge:sqlite-android:0.18.0:natives-x86")
    implementation("org.mapsforge:sqlite-android:0.18.0:natives-x86_64")
//    implementation("com.google.android.gms:play-services-maps:20.2.0")
    implementation("com.google.android.gms:play-services-maps:18.0.0")
    implementation("com.google.maps.android:android-maps-utils:2.3.0")
    implementation("com.google.firebase:firebase-auth:21.0.1")
    implementation("com.google.firebase:firebase-database:19.3.0")
    implementation("com.google.android.gms:play-services-auth:20.1.0")
    implementation("com.facebook.android:facebook-android-sdk:15.2.0")
//    implementation("com.github.smarteist:autoimageslider:1.4.0")
    implementation("com.github.smarteist:Android-Image-Slider:1.4.0")
    implementation("com.android.billingclient:billing:7.0.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("me.zhanghai.android.materialratingbar:library:1.4.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-auth:22.3.1")

    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.android.volley:volley:1.2.1")
    //    implementation("com.github.barteksc:android-pdf-viewer:2.8.2")
//    implementation("com.github.jorgealdana30:AndroidPdfViewerV2:2.8.3")

//    implementation("com.github.bsamidb:pdfviewer:1.0.1")

}