# [smkit-ui-android-demo](https://github.com/sency-ai/smkit-sdk)

## Table of contents
1. [ Installation ](#inst)
2. [ Setup ](#setup)
3. [ Configure ](#conf)
4. [ Start ](#start)
5. [ Data ](https://github.com/sency-ai/smkit-ui-android-demo/blob/main/DataTypes.md)

## 1. Installation <a name="inst"></a>

### Gradle
Here is the current available version of the SMKitUI project:

| Project | Version |
|---------|:-------:|
| smkitui |  0.2.6  |

At Sency we using different startegy with our Artifactories
In project level build.gradle please add our repo endpoint url
```groovy
allprojects {
    maven {
        url "https://artifacts.sency.ai/artifactory/release/"
    }
}
```

Sync and add the following dependency in app level build.gradle
```groovy
dependencies {
    implementation 'com.sency.smkitui:smkitui:$latest_version'
}
```

## 2. Setup <a name="setup"></a>
At AndroidManifest.xml the CAMERA permission has to be added
```xml
<!-- For using the camera -->
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature android:name="android.hardware.camera" />
<uses-feature android:name="android.hardware.camera.autofocus" />
```

At AndroidManifest.xml disable rotation of the app
```xml
<!-- For using the camera -->
<activity
    ...
    android:screenOrientation="portrait">
```

Our SDK targets minSdk 26, in your app level `build.gradle` please assert that.
```groovy
android {
    defaultConfig {
        minSdk 26
    }
}
```

## 3. Configure <a name="conf"></a>
```Kotlin
val smKitUI: SMKitUI = SMKitUI.Configuration(context)
    .setUIKey("YOUR_KEY")
    .configure(object : SMKitUIConfigurationListener {
        override fun onSuccess() {
            //The configuration was successful
        }

        override fun onFailure() {
            //The configuration failed with error
        }
    })
```
To reduce wait time we recommend to call `configure` on app launch.

**⚠️ SMKitUI will not work if you don't first call configure.**

## 4. Start <a name="start"></a>

- [Start Assessment](https://github.com/sency-ai/smkit-ui-android-demo/blob/main/Assessment.md)

- [Start Custom Workout/Assessment](https://github.com/sency-ai/smkit-ui-android-demo/blob/main/CustomWorkout.md)

------

Having issues? [Contact us](mailto:support@sency.ai) and let us know what the problem is.
