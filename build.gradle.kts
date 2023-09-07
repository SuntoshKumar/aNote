import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("io.realm.kotlin") version "1.10.0"
}

group = "com.sam.krish"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation("io.realm.kotlin:library-base:1.10.0")
    implementation("io.realm.kotlin:library-sync:1.10.0") // If using Device Sync
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0") // If using coroutines with
    implementation("com.itextpdf:itextg:5.5.10")
    implementation("org.jetbrains.compose.material:material-icons-extended-desktop:1.0.0")
    //implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:0.30.1")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg ,TargetFormat.Msi, TargetFormat.Exe, TargetFormat.Deb)
            packageName = "ANote"
            packageName ="Note"
//            packageName = "aNote"
//            version = "1.0.0"
//            appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))
//            description = "aNote application in using compose for desktop"
//            copyright = "© 2020 SamKrish. All rights reserved."
//            licenseFile.set(project.file("src/main/resources/LICENSE.txt"))


//            macOS {
//                // macOS specific options
//                iconFile.set(File("src/main/resources/mac_icon.icns"))
//                packageVersion = "1.0.0"
//                bundleID = "com.sam.krish"
//                packageName = "aNote"
//                dockName = "aNote"
//                dmgPackageBuildVersion = "1.0.0"
//                pkgPackageVersion = "1.0.0"
//                packageBuildVersion = "1.0.0"
//                dmgPackageBuildVersion = "1.0.0"
//                pkgPackageBuildVersion = "1.0.0"
//
//            }
//            windows {
//                // Windows specific options
//                iconFile.set(File("src/main/resources/window_icon.ico"))
//                packageVersion = "1.0.0"
//                console = true
//                dirChooser = true
//                perUserInstall = true
//                menuGroup = "start-menu-group"
//                msiPackageVersion = "1.0.0"
//                exePackageVersion = "1.0.0"
//            }
//            linux {
//                // Linux specific options
//                iconFile.set(File("src/main/resources/linux_icon.png"))
//                packageVersion = "1.0.0"
//                packageName = "com.sam.krish"
//                debMaintainer = "suntosemyanmar@gmail.com"
//                menuGroup = "Utility"
//                appRelease = "1"
//                debPackageVersion = "1.0.0"
//                rpmPackageVersion ="1.0.0"
//            }
        }
    }
}
