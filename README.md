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


<a name="start"></a>
## 3. Start
Implement **SMKitUIWorkoutListener**.
```Kotlin
class MainActivity : ComponentActivity(), SMKitUIWorkoutListener {
    // Runtime error callback
    override fun handleWorkoutErrors(error: Error) {
    }

    // Workout session end callback
    override fun workoutDidFinish(summary: WorkoutSummaryData) {
    }

    // Exit workout callback
    overrice fun didExitWorkout(summary: WorkoutSummaryData) {
    }
    
    // When exercise is finished
    override fun exerciseDidFinish(data: ExerciseData) {
    }
}
```

### Start (Custom) Assessment
**startAssessment** starts one of Sency's blueprint assessments or your Custom Assessment.
```Kotlin
fun startAssessment() {
    try {
        // In order to start Sency's blueprint assessment
        smKitUI.startAssessment(listener = this)
        // In order to start your own custom assessment
        smKitUI.startAssessment(Custom("YOUR_CUSTOM_ID"), listener = this)
    } catch (e: Exception) {
        println("startAssessment: $e")
    }
}
```

### Start Custom Workout
**startWorkout** starts a custom workout.
```Kotlin
// List of exercises
val intro = applicationContext.getRawResourceUri(R.raw.customWorkoutIntro)
val soundtrack = applicationContext.getRawResourceUri(R.raw.fullBodyLong)
val exercises: List<SMExercise> = listOf(
    SMExercise(
        prettyName = "Squat",
        exerciseIntro = "exerciseIntro_SquatRegular_60",
        totalSeconds = 60,
        introSeconds = 5,
        videoInstruction = "SquatRegularInstructionVideo",
        uiElements = setOf(UiElement.Timer, UiElement.GaugeOfMotion),
        detector = "SquatRegular",
        repBased = true,
        exerciseClosure = "exerciseClosure_0_0.mp3",
        scoringParams = ScoringParams(
            targetRom = null,
            targetTime = 0,
            scoreFactor = 0.5f,
            passCriteria = null,
            type = "time",
            targetReps = 60
        ),
    ),
    SMExercise(
        prettyName = "Push-ups",
        totalSeconds = 60,
        introSeconds = 8,
        exerciseIntro = "exerciseIntro_PushupRegular_60",
        videoInstruction = "PushupRegularInstructionVideo",
        uiElements = setOf(UiElement.RepsCounter, UiElement.Timer),
        detector = "PushupRegular",
        repBased = true,
        exerciseClosure = "",
        scoringParams = ScoringParams(
            targetRom = null,
            targetTime = 0,
            scoreFactor = 0.5f,
            passCriteria = null,
            type = "time",
            targetReps = 60
        ),
    )
)
 val smWorkout = SMWorkout(
    id = "50",
    name = "demo workout",
    workoutIntro = "introduction",
    soundtrack = "soundtrack_7",
    exercises = viewModel.exercies(),
    workoutClosure = "workoutClosure.mp3",
    getInFrame = "bodycal_get_in_frame",
    bodycalFinished = "bodycal_finished"
)
smKitUI.startWorkout(smWorkout, object: SMKitUIWorkoutListener{})
```

Having issues? [Contact us](mailto:support@sency.ai) and let us know what the problem is.
