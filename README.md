# SMKitUI Android Demo

This application is the customer-facing reference for SMKitUI 1.8.0 camera-based assessments and workouts. It demonstrates SDK configuration, every supported session entry point, runtime options, per-exercise options, result callbacks, and advanced 1.8.0 exercise features in one small Android app.

## What the demo covers

- Standard fitness assessment
- Customized assessment
- Customized workout and continuation workout
- Workout from a Sency program ID
- Runtime configuration and reconfiguration
- Guidance mode, guidance suggestions, and guidance diagnostics
- Small-body-part focus and variation mismatch feedback
- Exercise summary timing metrics and assessment insights
- Counter, completion, language, instruction-video, audio, and pause behavior
- Adaptive ROM, stretch sets, position repetitions, target progress, and exercise display sections/circuits
- UI colors, skeleton presets, feedback exclusions, and SDK configuration JSON
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

## Install SMKitUI 1.8.0

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
    implementation "com.sency.smkitui:smkitui:1.8.0"
    implementation "com.sency.smkit:smkit:1.8.0"
    implementation "com.sency.smbase.nativeclient:smbase-native-client:1.8.0"
}
```

The explicit SMKit dependency supplies `PoseModelChoice`, which is part of SMKitUI's public configuration surface. The native-client dependency supplies the public feedback model used by typed feedback-exclusion APIs.

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
- Position-based exercises can use `SMPositionRepConfig`.
- Target-rep exercises can display target progress and play target-completion vocals.
- `internalInsightsKey` and `exportInternalInsights` demonstrate insight attribution/export.
- `SMExerciseDisplayContext` groups exercises into named sections and circuits.

## Test against an unpublished local SDK

The demo normally resolves artifacts from Sency Artifactory. SDK maintainers can verify a local publication without editing repository URLs:

```bash
./gradlew assembleDebug -PsmkitLocalRepo=/absolute/path/to/smkit_android/repo
```

The equivalent environment variable is `SMKIT_LOCAL_REPO`. This repository is inserted before the remote repository only when explicitly supplied.

## 1.8.0 integration notes

- Minimum Android API is 24.
- Guidance-mode suggestions, reset/rearm behavior, and small-body-part focus are represented in the demo.
- Assessment insights and exercise timing metrics are configurable.
- Per-exercise target completion voice, intent voice, position reps, display context, and internal insight keys are represented.

For support or a program ID, contact [support@sency.ai](mailto:support@sency.ai).
