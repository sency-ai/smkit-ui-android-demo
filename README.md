# SMKitUI Android Demo

Welcome to the Sency SMKitUI Android SDK demo project! This guide will help you get started quickly and provide links to detailed documentation for each SDK feature.

## 🚀 Quick Start
1. **Install the SDK**
2. **Configure your app**
3. **Start using SMKitUI features**

## 📦 Installation
Add Sency's Maven repo to your project-level `build.gradle`:
```groovy
allprojects {
    maven {
        url "https://artifacts.sency.ai/artifactory/release/"
    }
}
```
Add the dependency to your app-level `build.gradle`:
```groovy
dependencies {
    implementation 'com.sency.smkitui:smkitui:1.3.9'
}
```

## ⚙️ Setup
- Add CAMERA permission to your `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature android:name="android.hardware.camera" />
<uses-feature android:name="android.hardware.camera.autofocus" />
```
- Lock screen orientation to portrait for the main activity.
- Set `minSdk` to 26 in your app-level `build.gradle`.

### 📱 Android 15 (16KB Page Size) Compatibility

SMKitUI v1.3.9+ is fully compatible with **Android 15's 16KB page size requirements**. 

#### Prerequisites for Android 15 Support:
To ensure compatibility with Android 15 and 16KB page size devices, your project must meet these requirements:

1. **Minimum Gradle Version**: Gradle 8.4 or higher
   ```groovy
   // gradle/wrapper/gradle-wrapper.properties
   distributionUrl=https\://services.gradle.org/distributions/gradle-8.13-bin.zip
   ```

2. **Android Gradle Plugin**: Version 8.0 or higher
   ```groovy
   // project-level build.gradle
   classpath 'com.android.tools.build:gradle:8.11.1'
   ```

3. **Kotlin Version**: 2.0 or higher (for AGP 8.11+)
   ```groovy
   // project-level build.gradle
   classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.21"
   ```

4. **Java Compatibility**: Java 17
   ```groovy
   // app-level build.gradle
   compileOptions {
       sourceCompatibility JavaVersion.VERSION_17
       targetCompatibility JavaVersion.VERSION_17
   }
   kotlinOptions {
       jvmTarget = '17'
   }
   ```

5. **Target SDK**: compileSdk 36 and targetSdk 36
   ```groovy
   android {
       compileSdk 36
       defaultConfig {
           targetSdk 36
       }
   }
   ```

The library has been optimized to work seamlessly on devices running Android 15, including those with 16KB page size configurations. No additional configuration beyond the prerequisites is required - the SDK automatically handles the necessary optimizations for optimal performance across all Android versions.

## 🔑 Configuration
Call `configure` on app launch:
```kotlin
val smKitUI: SMKitUI = SMKitUI.Configuration(context)
    .setUIKey("YOUR_KEY")
    .configure(object : SMKitUIConfigurationListener {
        override fun onSuccess() { /* Success */ }
        override fun onFailure() { /* Failure */ }
    })
```
> **SMKitUI will not work if you don't first call configure.**

## 🏁 Main Features & Guides
- [Start Assessment](./StartAssessment.md)
- [Start Customized Assessment](./StartCustomizedAssessment.md)
- [Start Customized Workout](./StartCustomizedWorkout.md)
- [Workout From Program](./StartWorkoutFromProgram.md)
- [Session Options](./SessionOptionsFull.md)
- [Data Types & Results](./DataTypes.md)

## 🧩 Advanced Topics
- [Session Options](./SessionOptionsFull.md)
- [Data Types](./DataTypes.md)

## 📝 Changelog

### Version 1.3.9 (Latest)
- ✅ **Android 15 (16KB Page Size) Compatibility**: Full support for Android 15's 16KB page size requirements
- 🔧 Updated native libraries optimized for 16KB page alignment
- 🚀 Enhanced stability and performance across all Android versions
- ⚙️ Requires Gradle 8.4+, AGP 8.0+, and Kotlin 2.0+ for full Android 15 support

### Version 1.3.8
- ✅ Initial Android 15 compatibility improvements
- 🔧 Performance optimizations for enhanced stability

## 🆘 Troubleshooting & Support
- Always call `configure` before starting any session
- If you have issues, [contact us](mailto:support@sency.ai)

---

Enjoy building with Sency SMKitUI!
