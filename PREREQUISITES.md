# SMKitUI Android prerequisites

SMKitUI 1.8.2 supports Android API 24 and later and includes native libraries compatible with Android's 16 KB page-size requirements.

## Build requirements

- minSdk 24
- compileSdk and targetSdk 36 (recommended and used by this demo)
- Gradle 8.13
- Android Gradle Plugin 8.11.1
- Kotlin 2.0.21
- Java 17

```groovy
android {
    compileSdk 36

    defaultConfig {
        minSdk 24
        targetSdk 36
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}
```

## Repository and dependencies

```groovy
repositories {
    google()
    mavenCentral()
    maven { url "https://artifacts.sency.ai/artifactory/release" }
}

dependencies {
    implementation "com.sency.smkitui:smkitui:1.8.2"
    implementation "com.sency.smkit:smkit:1.8.2"
    implementation "com.sency.smbase.nativeclient:smbase-native-client:1.8.2"
}
```

The SMKit dependency exposes `PoseModelChoice`; the native-client dependency exposes public feedback model types used by APIs such as `setFeedbacksUIToExclude`.

The published compatibility floor is CameraX 1.1.0, AppCompat 1.4.2, and Kotlin Coroutines 1.5.0. CameraX's managed APIs remain at 1.1.0, while `camera-core` resolves to Sency's `1.1.0.1-sency16kb` compatibility artifact so the native helper supports Android 16 KB page sizes. The demo pins those versions explicitly in `app/build.gradle` to validate the floor against newer transitive dependency requests.

## Permissions and features

```xml
<uses-permission android:name="android.permission.CAMERA" />

<uses-feature android:name="android.hardware.camera" />
<uses-feature android:name="android.hardware.camera.autofocus" />
```

Camera permission is required and must be requested at runtime before starting an SDK session.

Lock the hosting activity to portrait:

```xml
<activity
    android:name=".MainActivity"
    android:screenOrientation="portrait" />
```

## SDK key

Put the key in the untracked `local.properties` file:

```properties
sdk_auth_key=your_sency_sdk_key_here
```

## Verify

For a published release:

```bash
./gradlew clean assembleDebug
```

For an SDK build published to a local Maven directory:

```bash
./gradlew assembleDebug -PsmkitLocalRepo=/absolute/path/to/smkit_android/repo
```

No additional runtime work is required for 16 KB page-size support.
