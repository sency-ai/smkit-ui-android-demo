## Custom Assessment

- **Definition**: Custom Assessments are programmatically created assessment flows in SMKitUI where scoring and summarization are essential.
- **Key Characteristics**:
  - The `ScoringParams` parameter inside the `SMExercise` object is **mandatory**.
  - Includes a **Summary Screen** by default.
  - The **Summary Screen** can be configured by passing the `showSummary` Boolean argument when starting the assessment.
- **ScoringParams Constraints**:
  - **General Constraints**:
    - `scoreFactor`:
      - Must not be `null`.
      - Must be in the range `0 ≤ scoreFactor ≤ 1`.
      - Throws `ScoreFactorInvalid()` if invalid.
  - **Type-specific Constraints**:
    - For `"rom"` type:
      - `targetRom` must not be `null`; throws `MissingTargetRom()` if not provided.
      - `targetRom` must be valid according to `FormFeedbackType`; throws `TargetRomInvalid()` if invalid.
      - Cannot be used with dynamic exercises (`SMBaseExerciseType.Dynamic`); throws `ScoringTypeInvalid()` if mismatched.
    - For `"time"` type:
      - `targetTime` must not be `null`; throws `MissingTargetTime()` if not provided.
      - `targetTime` must be greater than `0`; throws `TargetTimeInvalid()` if invalid.
      - Cannot be used with dynamic exercises (`SMBaseExerciseType.Dynamic`); throws `ScoringTypeInvalid()` if mismatched.
    - For `"reps"` type:
      - `targetReps` must not be `null`; throws `MissingTargetReps()` if not provided.
      - `targetReps` must be greater than `0`; throws `TargetRepsInvalid()` if invalid.
      - Must be used with dynamic exercises (`SMBaseExerciseType.Dynamic`); throws `ScoringTypeInvalid()` if mismatched.
- **Initialization**:  
  To start a Custom Assessment, create an `SMWorkout` object. You can also pass custom assets to the exercises.

# Example Assessment: 
```Kotlin
// List of exercises
val cacheFile = context?.cachedir
val localFullPath = "$cacheFile/FULL/PATH"
val onlineUrl = "https://www.youronlinefilename.mp3/4"

val exercises: List<SMExercise> = listOf(
  SMExercise( //2
    prettyName = "Lunge Front Right",
    totalSeconds = 60,
    introSeconds = 5,
    videoInstruction = "LungeFrontRight",
    uiElements = setOf(UiElement.timer, UiElement.repsCounter),
    detector = "LungeSideRight",
    repBased = true,
    scoringParams = ScoringParams(
      targetReps = 54,
      scoreFactor = 0.5f,
      passCriteria = null,
      targetRom = null,
      targetTime = null,
      type = "reps"
    ),
    summaryTitleMainMetric = "Info for the SummaryPage",
    summaryTitle = "Info for the SummaryPage",
    summarySubTitle = "Info for the SummaryPage",
    summarySubTitleMainMetric = "Info for the SummaryPage",
    exerciseIntro = "YOUR_ASSET",
    exerciseClosure = "YOUR_ASSET",
    side = "right"
  ),
  SMExercise( //3
    prettyName = "Lunge Front Left",
    totalSeconds = 60,
    introSeconds = 5,
    videoInstruction = "LungeFrontLeft",
    uiElements = setOf(UiElement.timer, UiElement.repsCounter),
    detector = "LungeSideLeft",
    repBased = true,
    scoringParams = ScoringParams(
      targetReps = 54,
      scoreFactor = 0.5f,
      passCriteria = null,
      targetRom = null,
      targetTime = null,
      type = "reps"
    ),
    summaryTitleMainMetric = "Info for the SummaryPage",
    summaryTitle = "Info for the SummaryPage",
    summarySubTitle = "Info for the SummaryPage",
    summarySubTitleMainMetric = "Info for the SummaryPage",
    exerciseIntro = "YOUR_ASSET",
    exerciseClosure = "YOUR_ASSET",
    side = "left"
  ),
  SMExercise( //4
    prettyName = "Standing Knee Raise Right",
    totalSeconds = 60,
    introSeconds = 5,
    videoInstruction = "StandingKneeRaiseRight",
    uiElements = setOf(UiElement.timer, UiElement.gaugeOfMotion),
    detector = "StandingKneeRaiseRight",
    repBased = false,
    scoringParams = ScoringParams(
      targetReps = null,
      scoreFactor = 0.5f,
      passCriteria = null,
      targetRom = "StandingKneeRaiseElevation",
      targetTime = null,
      type = "rom"
    ),
    summaryTitleMainMetric = "Info for the SummaryPage",
    summaryTitle = "Info for the SummaryPage",
    summarySubTitle = "Info for the SummaryPage",
    summarySubTitleMainMetric = "Info for the SummaryPage",
    exerciseIntro = "YOUR_ASSET",
    exerciseClosure = "YOUR_ASSET",
    side = "right"
  ),
  SMExercise( //5
    prettyName = "Standing Knee Raise Left",
    totalSeconds = 60,
    introSeconds = 5,
    videoInstruction = "StandingKneeRaiseLeft",
    uiElements = setOf(UiElement.timer, UiElement.gaugeOfMotion),
    detector = "StandingKneeRaiseLeft",
    repBased = false,
    scoringParams = ScoringParams(
      targetReps = null,
      scoreFactor = 0.5f,
      passCriteria = null,
      targetRom = "StandingKneeRaiseElevation",
      targetTime = null,
      type = "rom"
    ),
    summaryTitleMainMetric = "Info for the SummaryPage",
    summaryTitle = "Info for the SummaryPage",
    summarySubTitle = "Info for the SummaryPage",
    summarySubTitleMainMetric = "Info for the SummaryPage",
    exerciseIntro = "YOUR_ASSET",
    exerciseClosure = "YOUR_ASSET",
    side = "left"
  ),
  SMExercise( //6
    prettyName = "Squat Regular Overhead Static",
    totalSeconds = 20,
    introSeconds = 5,
    videoInstruction = "SquatRegularOverheadStatic",
    uiElements = setOf(UiElement.timer, UiElement.gaugeOfMotion),
    detector = "SquatRegularOverheadStatic",
    repBased = true,
    scoringParams = ScoringParams(
      targetReps = 20,
      scoreFactor = 0.9f,
      passCriteria = null,
      targetRom = null,
      targetTime = 10,
      type = "time"
    ),
    summaryTitleMainMetric = "Info for the SummaryPage",
    summaryTitle = "Info for the SummaryPage",
    summarySubTitle = "Info for the SummaryPage",
    summarySubTitleMainMetric = "Info for the SummaryPage",
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

## Custom Workout

- **Definition**: Custom Workouts are flexible, programmatically defined flows in SMKitUI tailored for workout purposes.
- **Key Characteristics**:
    - The `ScoringParams` parameter inside the `SMExercise` object is **not allowed** execption will be thrown.
    - By default, there is **no Summary Screen** included.
- **Initialization**:  
  To start a Custom Workout, create an `SMWorkout` object and pass custom assets to the exercises.  

```Kotlin
// List of exercises
val cacheFile = context?.cachedir
val localFullPath = "$cacheFile/FULL/PATH"
val onlineUrl = "https://www.youronlinefilename.mp3/4"

val exercises: List<SMExercise> = listOf(
    SMExercise(
        prettyName = "Squat",
        exerciseIntro = localFullPath,
        totalSeconds = 60,
        introSeconds = 5,
        videoInstruction = onlineUrl,
        uiElements = setOf(UiElement.Timer, UiElement.GaugeOfMotion),
        detector = "SquatRegular",
        repBased = true,
        exerciseClosure = "exerciseClosure_0_0.mp3",
        scoringParams = null,
        summaryTitle = "",
        summarySubTitle = "",
        summaryTitleMainMetric = "",
        summarySubTitleMainMetric = "",
        side = ""
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
        scoringParams = null,
        summaryTitle = "",
        summarySubTitle = "",
        summaryTitleMainMetric = "",
        summarySubTitleMainMetric = "",
        side = ""
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

smKitUI.startCustomizedWorkout(smWorkout, object : SMKitUIWorkoutListener {})
```