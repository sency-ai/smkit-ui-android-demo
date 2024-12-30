# Customized Assessment

> The customized assessment enables you to create a personalized evaluation using the exercises and movements from our [Movement catalog](https://github.com/sency-ai/smkit-sdk/blob/main/SDK-Movement-Catalog.md), tailored to your professional standards or personal preferences.
> Exercises are programmatically. please refer to the [Constraints](#cons) Section Below

- **Initialization**:  
    To start a Custom Assessment, create an `SMWorkout` object.


- **SMWorkout**
  In order to create `SMWorkout` object you need to pass your own assets (audios & videos) to the workout object.
```kotlin
val smWorkout = SMWorkout(
    id = "50", // String | Your workout ID 
    name = "demo workout", // String | Your workout Name
    workoutIntro = "YOUR_ASSET", // String | .mp3 https url or R.raw.sample 
    soundtrack = "YOUR_ASSET", // String | .mp3 https url or R.raw.sample 
    exercises = exercies(), // List<SMExercise> | your programmatically built exercises
    workoutClosure = "YOUR_ASSET", // String | .mp3 http url or R.raw.sample 
    getInFrame = "YOUR_ASSET", // String | .mp3 http url or R.raw.sample 
    bodycalFinished = "YOUR_ASSET" // String | .mp3 http url or R.raw.sample 
)
```

- **SMExercise**
  In order to create `SMExericse` object you need to pass your own assets (audios & videos) to the exercise object.
```kotlin
val smExercise = SMExercise(
    prettyName = "High Knees", // String | Your pretty name to the exercise
    totalSeconds = 20, // Int | Total seconds the exercise will run
    videoInstruction = "YOUR_ASSET", // String | .mp3 https url or R.raw.sample 
    uiElements = setOf(UiElement.timer, UiElement.repsCounter),
    detector = "HighKnees", // String | from our catalog of exercises
    scoringParams = ScoringParams( // ScoringParams | please check Constraints Section Below
        targetReps = 10,
        scoreFactor = 0.5f,
        passCriteria = null,
        targetRom = null,
        targetTime = null,
        type = "reps"
    ),
    summaryMainMetricTitle = "Info for the SummaryPage", // String | Exercise' main metric title in Summary Page
    summaryTitle = "Info for the SummaryPage", // String | Exercise' Title in Summary Page
    summarySubTitle = "Info for the SummaryPage", // String | Exercise' SubTitle in Summary Page
    summaryMainMetricSubTitle = "Info for the SummaryPage", // String | Exercise' title in Summary Page
    exerciseIntro = com.sency.smkitapp.R.raw.simple.toString(), // String | .mp3 https url or R.raw.sample 
    exerciseClosure = null, // String | .mp3 https url or R.raw.sample 
    side = null // String | Left Or Right. 
    // if you want to pair two exercises to be shown together inside summary page give them the same detector name only postfix changes to Right or Left.
    // and accordingly inside the side parameter
)
```


# Customized Assessment Example: 
```Kotlin
val exercises: List<SMExercise> = listOf(
  SMExercise(
    prettyName = "High Knees",
    totalSeconds = 20,
    videoInstruction = "HighKnees",
    uiElements = setOf(UiElement.timer, UiElement.repsCounter),
    detector = "HighKnees",
    scoringParams = ScoringParams(
      targetReps = 10,
      scoreFactor = 0.5f,
      passCriteria = null,
      targetRom = null,
      targetTime = null,
      type = "reps"
    ),
    summaryMainMetricTitle = "Info for the SummaryPage",
    summaryTitle = "Info for the SummaryPage",
    summarySubTitle = "Info for the SummaryPage",
    summaryMainMetricSubTitle = "Info for the SummaryPage",
    exerciseIntro = com.sency.smkitapp.R.raw.simple.toString(),
    exerciseClosure = null,
    side = null
  ),
  SMExercise(
    prettyName = "Lunge Front Right",
    totalSeconds = 60,
    videoInstruction = "LungeFrontRight",
    uiElements = setOf(UiElement.timer, UiElement.repsCounter),
    detector = "LungeSideRight",
    scoringParams = ScoringParams(
      targetReps = 54,
      scoreFactor = 0.5f,
      passCriteria = null,
      targetRom = null,
      targetTime = null,
      type = "reps"
    ),
    summaryMainMetricTitle = "Info for the SummaryPage",
    summaryTitle = "Info for the SummaryPage",
    summarySubTitle = "Info for the SummaryPage",
    summaryMainMetricSubTitle = "Info for the SummaryPage",
    exerciseIntro = "YOUR_ASSET",
    exerciseClosure = "YOUR_ASSET",
    side = "right"
  ),
  SMExercise(
    prettyName = "Lunge Front Left",
    totalSeconds = 60,
    videoInstruction = "LungeFrontLeft",
    uiElements = setOf(UiElement.timer, UiElement.repsCounter),
    detector = "LungeSideLeft",
    scoringParams = ScoringParams(
      targetReps = 54,
      scoreFactor = 0.5f,
      passCriteria = null,
      targetRom = null,
      targetTime = null,
      type = "reps"
    ),
    summaryMainMetricTitle = "Info for the SummaryPage",
    summaryTitle = "Info for the SummaryPage",
    summarySubTitle = "Info for the SummaryPage",
    summaryMainMetricSubTitle = "Info for the SummaryPage",
    exerciseIntro = "YOUR_ASSET",
    exerciseClosure = "YOUR_ASSET",
    side = "left"
  ),
  SMExercise(
    prettyName = "Standing Knee Raise Right",
    totalSeconds = 60,
    videoInstruction = "StandingKneeRaiseRight",
    uiElements = setOf(UiElement.timer, UiElement.gaugeOfMotion),
    detector = "StandingKneeRaiseRight",
    scoringParams = ScoringParams(
      targetReps = null,
      scoreFactor = 0.5f,
      passCriteria = null,
      targetRom = "StandingKneeRaiseElevation",
      targetTime = null,
      type = "rom"
    ),
    summaryMainMetricTitle = "Info for the SummaryPage",
    summaryTitle = "Info for the SummaryPage",
    summarySubTitle = "Info for the SummaryPage",
    summaryMainMetricSubTitle = "Info for the SummaryPage",
    exerciseIntro = "YOUR_ASSET",
    exerciseClosure = "YOUR_ASSET",
    side = "right"
  ),
  SMExercise(
    prettyName = "Standing Knee Raise Left",
    totalSeconds = 60,
    videoInstruction = "StandingKneeRaiseLeft",
    uiElements = setOf(UiElement.timer, UiElement.gaugeOfMotion),
    detector = "StandingKneeRaiseLeft",
    scoringParams = ScoringParams(
      targetReps = null,
      scoreFactor = 0.5f,
      passCriteria = null,
      targetRom = "StandingKneeRaiseElevation",
      targetTime = null,
      type = "rom"
    ),
    summaryMainMetricTitle = "Info for the SummaryPage",
    summaryTitle = "Info for the SummaryPage",
    summarySubTitle = "Info for the SummaryPage",
    summaryMainMetricSubTitle = "Info for the SummaryPage",
    exerciseIntro = "YOUR_ASSET",
    exerciseClosure = "YOUR_ASSET",
    side = "left"
  ),
  SMExercise(
    prettyName = "Squat Regular Overhead Static",
    totalSeconds = 20,
    videoInstruction = "SquatRegularOverheadStatic",
    uiElements = setOf(UiElement.timer, UiElement.gaugeOfMotion),
    detector = "SquatRegularOverheadStatic",
    scoringParams = ScoringParams(
      targetReps = 20,
      scoreFactor = 0.9f,
      passCriteria = null,
      targetRom = null,
      targetTime = 10,
      type = "time"
    ),
    summaryMainMetricTitle = "Info for the SummaryPage",
    summaryTitle = "Info for the SummaryPage",
    summarySubTitle = "Info for the SummaryPage",
    summaryMainMetricSubTitle = "Info for the SummaryPage",
    exerciseIntro = "YOUR_ASSET",
    exerciseClosure = "YOUR_ASSET",
    side = null
  ),
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

smKitUI.startCustomizedAssessment(smWorkout, showSummary = true , object : SMKitUIWorkoutListener {})
```

---

## Customized Workout

- **Definition**: Customized Workouts are exactly like Customized Assessment, but their exercises expecting to get null inside `ScoringParams` and they don't show a Summary page. 
- **Key Characteristics**:
    - The `ScoringParams` parameter inside the `SMExercise` object is **not allowed** execption will be thrown.
    - By default, there is **no Summary Screen** included.
- **Initialization**:  
  To start a Custom Workout, follow the same steps from Customized Assessment but call:

```Kotlin
smKitUI.startCustomizedWorkout(smWorkout, object : SMKitUIWorkoutListener {})
```


## Constraints on Exercises <a name="cons"></a>
> - **Definition**: Custom Assessments are programmatically created assessment flows in SMKitUI where scoring and summarization are essential.
> - **Key Characteristics**:
    >   - The `ScoringParams` parameter inside the `SMExercise` object is **mandatory**.
>   - Includes a **Summary Screen** by default.
>   - The **Summary Screen** can be configured by passing the `showSummary` Boolean argument when starting the assessment.
> - **ScoringParams Constraints**:
    >   - **General Constraints**:
          >     - `scoreFactor`:
                  >       - Must not be `null`.
>       - Must be in the range `0 ≤ scoreFactor ≤ 1`.
>       - Throws `ScoreFactorInvalid()` if invalid.
>   - **Type-specific Constraints**:
      >     - For `"rom"` type:
              >       - `targetRom` must not be `null`; throws `MissingTargetRom()` if not provided.
>       - `targetRom` must be valid according to `FormFeedbackType`; throws `TargetRomInvalid()` if invalid.
>       - Cannot be used with dynamic exercises (`SMBaseExerciseType.Dynamic`); throws `ScoringTypeInvalid()` if mismatched.
>     - For `"time"` type:
        >       - `targetTime` must not be `null`; throws `MissingTargetTime()` if not provided.
>       - `targetTime` must be greater than `0`; throws `TargetTimeInvalid()` if invalid.
>       - Cannot be used with dynamic exercises (`SMBaseExerciseType.Dynamic`); throws `ScoringTypeInvalid()` if mismatched.
>     - For `"reps"` type:
        >       - `targetReps` must not be `null`; throws `MissingTargetReps()` if not provided.
>       - `targetReps` must be greater than `0`; throws `TargetRepsInvalid()` if invalid.
>       - Must be used with dynamic exercises (`SMBaseExerciseType.Dynamic`); throws `ScoringTypeInvalid()` if mismatched.
