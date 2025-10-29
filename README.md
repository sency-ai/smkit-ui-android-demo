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
    implementation 'com.sency.smkitui:smkitui:1.3.8'
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

### 📱 Android 15 Compatibility
SMKitUI v1.3.8+ is fully compatible with **Android 15's 16KB page size requirements**. The library has been optimized to work seamlessly on devices running Android 15, including those with 16KB page size configurations. No additional configuration is required on your part - the SDK automatically handles the necessary optimizations for optimal performance across all Android versions.

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

### Version 1.3.8 (Latest)
- ✅ **Android 15 Compatibility**: Full support for Android 15's 16KB page size requirements
- 🔧 Performance optimizations for enhanced stability
- 🚀 Improved compatibility across all Android versions

## 🆘 Troubleshooting & Support
- Always call `configure` before starting any session
- If you have issues, [contact us](mailto:support@sency.ai)

---

Enjoy building with Sency SMKitUI!
