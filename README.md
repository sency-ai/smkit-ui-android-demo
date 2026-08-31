# SMKitUI Android Demo

This application is the customer-facing reference for SMKitUI 1.8.2 camera-based assessments and workouts. It demonstrates SDK configuration, every supported session entry point, runtime options, per-exercise options, result callbacks, and advanced 1.8.2 exercise features in one small Android app.

## What the demo covers

- Built-in Fitness, Body 360, Cardio, and Strength assessments
- Customized assessment
- Customized workout and continuation workout
- Workout from a Sency program ID
- Runtime configuration and reconfiguration
- Guidance mode, guidance suggestions, and guidance diagnostics
- Small-body-part focus and variation mismatch feedback
- Exercise summary timing metrics and assessment insights
- Counter, completion, language, instruction-video, audio, and pause behavior
- Adaptive ROM, stretch sets, position repetitions, target progress, and exercise display sections/circuits
- Persisted demo settings for calibration, languages, model choice, UI colors, complete skeleton styling, pause actions, feedback exclusions, and SDK configuration text
- Workout callbacks, continuation callbacks, and internal-insight export

The main integration is in [`MainActivity.kt`](app/src/main/java/com/example/smkituidemoapp/MainActivity.kt). Per-exercise model examples are in [`MainViewModel.kt`](app/src/main/java/com/example/smkituidemoapp/viewModels/MainViewModel.kt).

## Requirements

- Android minSdk 24
- compileSdk/targetSdk 36
- Java 17
- Camera permission
- Portrait orientation for the activity that hosts SMKitUI
- A Sency SDK key

See [PREREQUISITES.md](PREREQUISITES.md) for the complete build setup.

## Install SMKitUI 1.8.2

Add the Sency repository:

```groovy
repositories {
    google()
    mavenCentral()
    maven { url "https://artifacts.sency.ai/artifactory/release" }
}
```

Add the SDK and the public feedback-model dependency used by typed feedback-exclusion APIs:

```groovy
dependencies {
    implementation "com.sency.smkitui:smkitui:1.8.2"
    implementation "com.sency.smkit:smkit:1.8.2"
    implementation "com.sency.smbase.nativeclient:smbase-native-client:1.8.2"
}
```

The explicit SMKit dependency supplies `PoseModelChoice`, which is part of SMKitUI's public configuration surface. The native-client dependency supplies the public feedback model used by typed feedback-exclusion APIs.

SMKitUI 1.8.2 is validated with CameraX 1.1.0, AppCompat 1.4.2, and Kotlin Coroutines 1.5.0. CameraX's managed APIs remain at 1.1.0; the SDK resolves `camera-core` to Sency's `1.1.0.1-sency16kb` compatibility artifact, which replaces only CameraX's native image-processing helper for Android 16 KB page-size support. This demo pins that exact compatibility graph so newer Material, Lifecycle, or ML Kit transitive requirements do not silently upgrade it. Navigation remains an application dependency and is not required by the SDK.

## Configure the demo

Create or update `local.properties` in the project root:

```properties
sdk_auth_key=your_sency_sdk_key_here
```

Do not commit a real key. The app exposes it as `BuildConfig.sdk_auth_key`.

SMKitUI must be configured before starting a session:

```kotlin
val smKitUI = SMKitUI.Configuration(applicationContext)
    .setUIKey(BuildConfig.sdk_auth_key)
    .setPoseModelChoice(PoseModelChoice.AdaptiveChoice)
    .setConfigureLanguage(SMLanguage.English)
    .setInstructionVideoConfig(
        InstructionVideoConfig(displayMode = VideoDisplayMode.DEFAULT)
    )
    .setGuidanceModeSuggestionEnabled(true)
    .setSmallBodyPartFocusEnabled(true)
    .setExerciseSummaryTimingMetricsEnabled(true)
    .setIncludeAssessmentInsights(true)
    .configure(configurationListener)
```

The demo's settings panel drives both configuration-time and runtime setters. Press **Apply / reconfigure SDK** after changing an option that is consumed during configuration.

The selected pose model, assessment-insight download, exercise timing metrics, and other configuration-builder values take effect after reconfiguration. Runtime setters such as theme, skeleton styling, language, pause actions, audio, and timer behavior apply immediately and are also included on the next configuration.

## Native demo features

The app makes the Android 1.8.2 session and configuration APIs directly discoverable:

- **Built-in assessment picker** starts Fitness, Body 360, Cardio, or Strength.
- **Custom assessment** demonstrates reps, time, and ROM scoring.
- **Custom workout** demonstrates a main workout plus a continuation workout.
- **SDK Features Demo** provides a selectable Android detector catalog and wires advanced per-exercise properties, including wide-angle camera presentation.
- **Settings** persists pose-model, appearance, skeleton, language, audio, calibration, pause, and session-behavior choices between launches.

The exercise picker uses an explicit catalog versioned for this Android 1.8.2 demo.

## Model and asset delivery

SMKitUI 1.8.2 downloads required SDK configuration, models, and session assets from the server and stores valid downloads in the app cache. Keep the device online for first configuration and for any session whose assets have not been cached. A valid prior server-derived cache can be reused when a refresh is unavailable; it is not a substitute for completing the initial online setup.

## Start a session

The home screen has runnable examples for all four public entry points:

```kotlin
smKitUI.startAssessment(
    assessmentType = Fitness,
    listener = listener,
    userData = UserData(14, Gender.Male),
    showSummary = true,
    modifications = null,
    showPhoneCalibration = true,
)

smKitUI.startCustomizedAssessment(
    workout = customAssessment,
    showSummary = true,
    listener = listener,
    modifications = null,
    showPhoneCalibration = true,
)

smKitUI.startCustomizedWorkout(
    workout = customWorkout,
    listener = listener,
    modifications = null,
    showPhoneCalibration = true,
)

smKitUI.startWorkoutProgram(
    workoutConfig = WorkoutConfig(
        programId = "PROGRAM_ID_FROM_SENCY",
        week = 1,
        bodyZone = BodyZone.FullBody,
        difficultyLevel = DifficultyLevel.MidDifficulty,
        workoutDuration = WorkoutDuration.Short,
        language = SMLanguage.English,
    ),
    listener = listener,
    modifications = null,
    showPhoneCalibration = true,
)
```

See the focused guides for session-model details:

- [Start assessment](StartAssessment.md)
- [Start customized assessment](StartCustomizedAssessment.md)
- [Start customized workout](StartCustomizedWorkout.md)
- [Start workout from a program](StartWorkoutFromProgram.md)
- [Session options](SessionOptionsFull.md)
- [Data types and results](DataTypes.md)

## SDK Features Demo screen

Open **SDK Features Demo** to select detectors and build a custom workout. The settings panel intentionally demonstrates the properties customers commonly need to discover:

- Guidance suggestions can be globally enabled and overridden per exercise.
- **Settings → Per-exercise options → Wide-angle camera** applies `useWideAngleCamera = true` to every selected non-Rest exercise so the device's widest supported field of view can be tested.
- Position-based exercises can use `SMPositionRepConfig`.
- Target-rep exercises can display target progress and play target-completion vocals.
- `internalInsightsKey` and `exportInternalInsights` demonstrate insight attribution/export.
- `SMExerciseDisplayContext` groups exercises into named sections and circuits.
- `SkeletonSettings` exposes presets, connection styles, joint shapes, colors, opacity, glow, width, softness, and animation duration.
- `setPauseTypes` demonstrates Resume, Skip, Start Over, Quit, Rest, and Switch.

See [Session options](SessionOptionsFull.md) for the complete Android configuration reference.

Wide angle is a nullable per-exercise option:

```kotlin
val exercise = SMExercise(/* ... */).also {
    it.useWideAngleCamera = true
}
```

`null` and `false` use standard mode. In wide mode SMKitUI requests the minimum zoom ratio reported by CameraX and shows the complete 4:3 camera frame. Devices that cannot zoom below 1× remain at 1× without failing the session. Calibration, Rest, and Cooldown continue to use standard mode.

## 1.8.2 integration notes

- Minimum Android API is 24.
- Guidance-mode suggestions, reset/rearm behavior, and small-body-part focus are represented in the demo.
- Assessment insights and exercise timing metrics are configurable.
- Per-exercise wide angle, target completion voice, intent voice, position reps, display context, and internal insight keys are represented.

For support or a program ID, contact [support@sency.ai](mailto:support@sency.ai).
