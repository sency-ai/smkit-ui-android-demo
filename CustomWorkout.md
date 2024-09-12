# Customized Workout and Assessment
With SMKitUI you can define, programmatically, a Customized Workout or Assessment
The key differance between the two is the `ScoringParams` parameter inside `SMExercise` object.
In Customized Workout `ScoringParams` is not mandatory. 
In Customized Assessment `ScoringParams` is mandatory.

### Start Customized Workout\Assessment
In order to start Custom Workout\Assessment please create a SMWorkout object. 
You can pass you custom assets to the exercises. 
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
        summaryTitleMainMetric = ""
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
        summaryTitleMainMetric = ""
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

smKitUI.startCustomizedAssessment(smWorkout, object : SMKitUIWorkoutListener {})
```
