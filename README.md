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

## ⚙️ Setup & Prerequisites

### Basic Setup
- Add CAMERA permission to your `AndroidManifest.xml`
- Lock screen orientation to portrait for activities using SMKitUI
- Set `minSdk` to 26 in your app-level `build.gradle`

### 📱 Android 15 (16KB Page Size) Compatibility

SMKitUI v1.3.9+ is fully compatible with **Android 15's 16KB page size requirements**. 

To ensure your project works with Android 15 devices, you must meet specific build configuration requirements including:
- Gradle 8.4+
- Android Gradle Plugin 8.0+
- Kotlin 2.0+
- Java 17 compatibility
- Target SDK 36

**📋 See [PREREQUISITES.md](./PREREQUISITES.md) for detailed setup instructions and requirements.**

The SDK automatically handles 16KB page size optimizations internally - no additional runtime configuration is needed beyond meeting the build prerequisites.

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
