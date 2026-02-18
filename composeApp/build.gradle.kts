import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlin.serialization)
//    alias(libs.plugins.androidKotlinMultiplatformLibrary) Will replace alias(libs.plugins.androidApplication)
}

kotlin {
//    androidLibrary {
//        androidResources {
//            enable = true
//        }
//    }
    jvmToolchain(21)

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    jvm()
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.material)
            implementation(libs.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            implementation(project(":shared"))

            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.material3)
            implementation(libs.ui)
            implementation(libs.components.resources)
            implementation(libs.ui.tooling.preview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.navigation.compose)
            implementation(libs.material.icons.extended)
            implementation(libs.ksafe)
            implementation(libs.ksafe.compose)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)
            implementation(libs.ktor.client.core)


        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
        }
    }
}

android {
    namespace = "com.kynarec.subsched"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.kynarec.subsched"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = libs.versions.app.version.get()
    }
    packaging {
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
                excludes += "/META-INF/INDEX.LIST"
                pickFirsts.add("META-INF/io.netty.versions.properties")
            }
        }
    }
    signingConfigs {
        create("release") {
            val keystorePath = System.getenv("KEYSTORE_FILE")
            if (keystorePath != null) {
                storeFile = file(keystorePath)
                storePassword = System.getenv("KEYSTORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        // build with ./gradlew assembleRelease
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            manifestPlaceholders["appName"] = "SubSched"
            if (System.getenv("CI") == "true" && System.getenv("KEYSTORE_FILE") == null) {
                error("Release keystore not configured")
            }
            if (System.getenv("KEYSTORE_FILE") != null) {
                signingConfig = signingConfigs.getByName("release")
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    dependenciesInfo {
        // Disables dependency metadata when building APKs.
        includeInApk = false
        // Disables dependency metadata when building Android App Bundles.
        includeInBundle = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures{
        compose = true
        buildConfig = true
        dataBinding {
            enable = true
        }
    }
    applicationVariants.all {
        outputs.all {
            val output = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            output.outputFileName = "SubSched_v${libs.versions.app.version.get()}.apk"
        }
    }

}

dependencies {
    debugImplementation(libs.ui.tooling)
}

// ./gradlew packageReleaseDistributionForCurrentOS
compose.desktop {
    application {
        mainClass = "com.kynarec.subsched.MainKt"

        buildTypes.release.proguard {
            optimize.set(true)
            configurationFiles.from(project.file("proguard-rules.pro"))
            obfuscate.set(true)
        }

        nativeDistributions {
            modules("java.base", "java.desktop", "jdk.unsupported", "java.scripting")
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "SubSched"
            packageVersion = libs.versions.app.version.get()
            windows {
                menu = true
                shortcut = true
                upgradeUuid = "ca7f9644-9275-405a-9083-258cd7196070"
                iconFile.set(project.file("src/commonMain/composeResources/files/subsched_icon.ico"))

            }
            linux {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/subsched_logo.png"))
            }
        }
    }
}
