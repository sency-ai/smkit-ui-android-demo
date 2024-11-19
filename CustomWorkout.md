## Custom Assessment

- **Definition**: Custom Assessments are programmatically created assessment flows in SMKitUI where scoring and summarization are essential.
- **Key Characteristics**:
    - The `ScoringParams` parameter inside the `SMExercise` object is **mandatory**.
    - Includes a **Summary Screen** by default.
    - The **Summary Screen** can be configured by passing the `showSummary` Boolean argument when starting the assessment.
- **Initialization**:  
  To start a Custom Assessment, create an `SMWorkout` object. You can also pass custom assets to the exercises.

# Example: 
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
        scoringParams = ScoringParams(
            targetRom = null,
            targetTime = 0,
            scoreFactor = 0.5f,
            passCriteria = null,
            type = "time",
            targetReps = 60
        ),
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
        scoringParams = ScoringParams(
            targetRom = null,
            targetTime = 0,
            scoreFactor = 0.5f,
            passCriteria = null,
            type = "time",
            targetReps = 60
        ),
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

smKitUI.startCustomizedAssessment(smWorkout, showSummary = true , object : SMKitUIWorkoutListener {})
```

---

## Custom Workout

- **Definition**: Custom Workouts are flexible, programmatically defined flows in SMKitUI tailored for workout purposes.
- **Key Characteristics**:
    - The `ScoringParams` parameter inside the `SMExercise` object is **not needed**.
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