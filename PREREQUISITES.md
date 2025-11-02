# Prerequisites for SMKitUI Android SDK

This document outlines the prerequisites required to use SMKitUI Android SDK, particularly for Android 15 (16KB page size) compatibility.

## Android 15 (16KB Page Size) Compatibility

Starting with **SMKitUI v1.3.9**, the SDK is fully compatible with Android 15's 16KB page size requirements. To ensure your project works correctly with Android 15 devices, you must meet the following requirements:

### 1. Minimum Gradle Version

**Gradle 8.4 or higher** is required for Android 15 compatibility.

Update your `gradle/wrapper/gradle-wrapper.properties`:
```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.13-bin.zip
```

### 2. Android Gradle Plugin (AGP)

**AGP 8.0 or higher** is required.

Update your project-level `build.gradle`:
```groovy
buildscript {
    dependencies {
        classpath 'com.android.tools.build:gradle:8.11.1'
    }
}
```

### 3. Kotlin Version

**Kotlin 2.0 or higher** is required when using AGP 8.11+.

Update your project-level `build.gradle`:
```groovy
buildscript {
    dependencies {
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.21"
    }
}
```

### 4. Java Compatibility

**Java 17** is required for AGP 8.0+.

Update your app-level `build.gradle`:
```groovy
android {
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = '17'
    }
}
```

### 5. Target SDK Version

**compileSdk 36 and targetSdk 36** are recommended for full Android 15 support.

Update your app-level `build.gradle`:
```groovy
android {
    compileSdk 36
    
    defaultConfig {
        targetSdk 36
        minSdk 26  // Minimum required by SMKitUI
    }
}
```

## Basic Requirements

### Minimum SDK Version
- **minSdk 26** (Android 8.0) or higher

### Required Permissions

Add these permissions to your `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature android:name="android.hardware.camera" />
<uses-feature android:name="android.hardware.camera.autofocus" />
```

### Screen Orientation

Lock screen orientation to portrait for activities using SMKitUI:
```xml
<activity
    android:name=".YourActivity"
    android:screenOrientation="portrait" />
```

## Maven Repository

Add Sency's Maven repository to your project-level `build.gradle`:
```groovy
allprojects {
    repositories {
        google()
        mavenCentral()
        maven {
            url "https://artifacts.sency.ai/artifactory/release"
        }
    }
}
```

## Verification

After updating your project to meet these prerequisites, verify your setup:

1. Clean your project:
   ```bash
   ./gradlew clean
   ```

2. Build your project:
   ```bash
   ./gradlew build
   ```

3. If you encounter any issues, ensure all the above prerequisites are correctly configured.

## Additional Notes

- The SDK automatically handles 16KB page size optimizations internally
- No additional runtime configuration is needed beyond meeting the build prerequisites
- These requirements ensure compatibility with both current Android versions and Android 15

## Support

If you have issues with prerequisites or configuration, [contact us](mailto:support@sency.ai).
