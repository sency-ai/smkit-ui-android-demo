# SMKitUI Android 1.8.1 session options

The demo settings panel is a runnable catalog of the Android APIs below. Settings are stored in `SharedPreferences`, applied through runtime setters when possible, and reused the next time the SDK is configured.

## Configuration-time options

Create and configure one `SMKitUI` instance before starting a session:

```kotlin
val smKitUI = SMKitUI.Configuration(applicationContext)
    .setUIKey(BuildConfig.sdk_auth_key)
    .setPoseModelChoice(PoseModelChoice.AdaptiveChoice)
    .setConfigureLanguage(SMLanguage.English)
    .setInstructionVideoConfig(InstructionVideoConfig())
    .setExerciseSummaryTimingMetricsEnabled(false)
    .setIncludeAssessmentInsights(false)
    .setSmallBodyPartFocusEnabled(false)
    .setGuidanceModeSuggestionEnabled(false)
    .setColorTheme(UIColorTheme.GREEN)
    .applySkeletonSettings { skeletonPreset = SkeletonPreset.DEFAULT }
    .configure(configurationListener)
```

`PoseModelChoice` supports `AdaptiveChoice`, `Prime`, `Pro`, `Lite`, `UltraLite`, and `Basic`. Prefer `AdaptiveChoice` unless Sency has recommended a fixed model for your device policy.

`setExerciseSummaryTimingMetricsEnabled` changes the exported exercise-summary shape. `setIncludeAssessmentInsights` downloads/evaluates the optional insight catalog; the assessment's `SMWorkout.exportInternalInsights` must also be true to export those insights.

## Built-in assessments and phone calibration

Android 1.8.1 exposes four built-in assessment categories:

```kotlin
val type: AssessmentType = Body360 // Fitness, Body360, Cardio, or Strength

smKitUI.startAssessment(
    assessmentType = type,
    userData = UserData(age = 28, gender = Gender.Female),
    showSummary = true,
    listener = listener,
    modifications = null,
    showPhoneCalibration = true,
)
```

The `showPhoneCalibration` argument is also available on customized assessments, customized workouts, and program workouts.

## Language

Session text/audio language and phone-calibration language are independent:

```kotlin
smKitUI.setSessionLanguage(SMLanguage.English)
smKitUI.setPhoneCalibrationLanguage(SMLanguage.Hebrew)
```

Android 1.8.1 publishes English and Hebrew.

## Pause actions

```kotlin
smKitUI.setPauseTypes(
    arrayOf(
        PauseDialogTypes.Resume,
        PauseDialogTypes.Skip,
        PauseDialogTypes.StartOver,
        PauseDialogTypes.Quit,
        PauseDialogTypes.Rest,
        PauseDialogTypes.Switch,
    )
)
```

Keep at least one action enabled. The demo falls back to Resume if every pause toggle is cleared.

## Completion and counting

```kotlin
smKitUI.setEndExercisePreferences(EndExercisePreference.TargetBased)
smKitUI.setCounterPreferences(CounterPreference.PerfectOnly)
```

Use target-based completion only when the exercise has the relevant `ScoringParams` target. `PerfectOnly` credits reps without detected form corrections.

## Session behavior

```kotlin
smKitUI.setIntelligenceRestEnabled(true)
smKitUI.setPhoneMovementCountPreventionEnabled(true)
smKitUI.setVariationMismatchFeedbackEnabled(true)
smKitUI.setStartTimerOnFirstActivity(true)
smKitUI.setWorkoutContinuationTimerDuration(10)
smKitUI.setEnableButtonTutorial(true)
```

`setStartTimerOnFirstActivity(true)` waits for the first rep on dynamic exercises or the first in-position frame on static, mobility, and body-assessment exercises.

## Audio

```kotlin
smKitUI.setPlayPhoneCalibrationAudio(true)
smKitUI.setPlayBodyCalibrationAudio(true)
smKitUI.setAllowAudioMixing(true)
smKitUI.setShowExternalAudioControl(true)
```

`setButtonTutorialCompletionAudioUri` accepts a host-provided `Uri?`. The demo passes `null`; an integrating app can supply its own audio URI when needed.

## Instruction video cycling

```kotlin
smKitUI.setInstructionVideoConfig(
    InstructionVideoConfig(
        displayMode = VideoDisplayMode.MEDIUM_CYCLE,
        mediumSizeCycles = 3,
    )
)
```

`mediumSizeCycles` must be between 1 and 5. `DEFAULT` minimizes after the instruction phase; `MEDIUM_CYCLE` keeps the video at medium size for the configured cycles before minimizing further.

## Theme and skeleton

```kotlin
smKitUI.setColorTheme(UIColorTheme.GREEN)
smKitUI.applySkeletonSettings {
    skeletonHidden = false
    skeletonPreset = SkeletonPreset.NEON_GLOW
    skeletonConnectionStyle = SkeletonConnectionStyle.SOLID
    skeletonJointShape = SkeletonJointShape.CIRCLE
    skeletonDotsOpacity = 1f
    skeletonConnectionsOpacity = 0.8f
    skeletonDotsInnerColorOption = SkeletonColorOption.WHITE
    skeletonDotsOuterColorOption = SkeletonColorOption.CYAN
    skeletonConnectionsInnerColorOption = SkeletonColorOption.WHITE
    skeletonConnectionsOuterColorOption = SkeletonColorOption.CYAN
    skeletonDotsGlow = 0.5f
    skeletonConnectionsGlow = 0.3f
    skeletonLineWidthScale = 1f
    skeletonOutlineScale = 1f
    skeletonSoftness = 0f
    skeletonAnimationDurationSeconds = 0.05f
}
```

Android exposes seven color themes, 26 skeleton presets, eight connection styles, six joint shapes, and selectable skeleton colors. The animation-duration property is clamped to 0–0.05 seconds.

## Guidance and feedback

```kotlin
smKitUI.setUseDefaultGuidanceMode(true)
smKitUI.setGuidanceModeSuggestionEnabled(true)
smKitUI.setGuidanceDebugLogging(BuildConfig.DEBUG)
smKitUI.setSmallBodyPartFocusEnabled(true)
smKitUI.setFeedbacksUIToExclude(setOf(FormFeedbackType.PushupKneesOnFloor))
smKitUI.setConfigString("CrunchesShallowDepth.low=0.25")
```

Guidance is applied only to detectors supported by the SDK's built-in policy. The demo uses a versioned Android detector catalog and lets the SDK validate detection when a session starts.

## Per-exercise options

`SMExercise` exposes the following Android 1.8.1 options. `MainViewModel.kt` populates the catalog-driven options; `guidanceVideoSegments` remains available for host-provided video timelines:

- `shortIntro`, `playPreExerciseCountdown`, and `playSoundOnEachRep`
- `playRepMilestoneVoice` and `repMilestoneInterval`
- `playTargetRepsCompletionVoice` and `intentVoiceFeedbackEnabled`
- `guidanceMode`, `enableGuidanceModeSuggestion`, and `guidanceVideoSegments`
- `enableSmallBodyPartFocus`
- `adaptiveRomFeedbackEnabled` and `adaptiveRomWarmupReps`
- `SMStretchSetConfig` and `SMPositionRepConfig`
- `showTargetProgress` and `internalInsightsKey`
- `SMExerciseDisplayContext` for sections, circuits, supersets, blocks, or custom groups

Android uses `UiElement.quickMotion` for the built-in quick-motion presentation.

## Workout continuation

```kotlin
val workout = SMWorkout(
    id = "demo",
    name = "Demo workout",
    workoutIntro = "",
    soundtrack = "",
    exercises = mainExercises,
    workoutClosure = "",
    getInFrame = "",
    bodycalFinished = "",
    continuation = SMWorkoutContinuation(
        introSoundKey = null,
        interactionUnlockSoundKey = "",
        exercises = continuationExercises,
    ),
)
```

## Android 1.8.1 scope

Every setting in this document maps to a public Android 1.8.1 type or method and is represented directly in the demo without reflection or placeholder controls.

## Results

All session choices feed the normal `SMKitUIWorkoutListener` callbacks:

- `exerciseDidFinish(data: ExerciseData)`
- `workoutDidFinish(summary: WorkoutSummaryData)`
- `didExitWorkout(summary: WorkoutSummaryData)`
- `handleWorkoutErrors(error: Error)`
- continuation prompt/choice callbacks

See [DataTypes.md](DataTypes.md) for the result structures.
