# [smkit-ui-android-demo](https://github.com/sency-ai/smkit-sdk)

1. [ Installation. ](#inst)
2. [ Configure. ](#conf)
3. [ Start. ](#start)

<a name="inst"></a>
## 1. Installation

### Gradle
Here is the current available version of the SMKitUI project:

| Project | Version |
|---------|:-------:|
| smkitui |  0.1.6  |

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

<a name="conf"></a>
## 2. Configure
```Kotlin
val smKitUI: SMKitUI = SMKitUI.Configuration(applicationContext)
    .setUIKey("YOUR_KEY")
    .configure(object : ConfigurationResult {
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

### [Start Assessment](https://github.com/sency-ai/smkit-ui-ios-demo/blob/main/Assessment.md)

### [Start Workout](https://github.com/sency-ai/smkit-ui-ios-demo/blob/main/Workout.md)

### [Start Custom Assessment](https://github.com/sency-ai/smkit-ui-ios-demo/blob/main/CustomAssessment.md)

Having issues? [Contact us](mailto:support@sency.ai) and let us know what the problem is.
