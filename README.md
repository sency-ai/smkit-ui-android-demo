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
| smkitui |  0.1.0  |

```groovy
dependencies {
    implementation 'com.sency.smkitui:$latest_version'
}
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
    fun handleWorkoutErrors(error: Error) {
        
    }

    // Workout session end callback
    fun workoutDidFinish(summary: WorkoutSummaryData) {
        
    }

    // Exit workout callback
    fun didExitWorkout(summary: WorkoutSummaryData) {
        
    }
}
```

### Start Assessment
**startAssessment** starts one of Sency's blueprint assessments.
```Kotlin
fun startAssessment() {
    try {
        smKitUI.startAssessment(this)
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
        name = "High Knees",
        exerciseIntro = null, // Custom sound,
        totalSeconds = 30,
        introSeconds = 5,
        videoInstruction = applicationContext.getRawResourceUri(R.raw.HighKnees),
        uiElements = setOf(UiElement.RepsCounter, UiElement.Timer),
        detector = "HighKnees",
        repBased = true,
        exerciseClosure = null // Custom sound
    ),
    SMExercise(
        name = "Squat Regular Static",
        exerciseIntro = null, // Custom sound,
        totalSeconds = 30,
        introSeconds = 5,
        videoInstruction = applicationContext.getRawResourceUri(R.raw.SquatRegularStatic),
        uiElements = setOf(UiElement.GaugeOfMotion, UiElement.Timer),
        detector = "SquatRegularStatic",
        repBased = false,
        exerciseClosure = null // Custom sound
    ),
    SMExercise(
        name = "Plank High Static",
        exerciseIntro = null, // Custom sound,
        totalSeconds = 30,
        introSeconds = 5,
        videoInstruction = applicationContext.getRawResourceUri(R.raw.PlankHighStatic),
        uiElements = setOf(UiElement.GaugeOfMotion, UiElement.Timer),
        detector = "PlankHighStatic",
        repBased = false,
        exerciseClosure = null // Custom sound
    ),
)
val smWorkout = SMWorkout(
    id = "",
    name = "TEST",
    workoutIntro = intro,
    soundtrack = soundtrack,
    exercises = exercises,
    workoutClosure = null // Custom sound
)

try {
    smKitUI.startWorkout(smWorkout, this)
} catch (e: Exception) {
    println("startAssessment: $e")
}
```

Having issues? [Contact us](support@sency.ai) and let us know what the problem is.