## Customized Workout

- **Definition**: Customized Workouts are exactly like Customized Assessment, but their exercises expecting to get null inside `ScoringParams` and they don't show a Summary page. 
- **Key Characteristics**:
    - The `ScoringParams` parameter inside the `SMExercise` object is **not allowed** execption will be thrown.
    - By default, there is **no Summary Screen** included.
- **Initialization**:  
  To start a Custom Workout, follow the same steps from Customized Assessment but call:


# Customized Assessment Example:
```Kotlin
val exercises: List<SMExercise> = listOf(
  SMExercise(
    prettyName = "High Knees",
    totalSeconds = 20,
    videoInstruction = "HighKnees",
    uiElements = setOf(UiElement.timer, UiElement.repsCounter),
    detector = "HighKnees",
    scoringParams = null,
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
    scoringParams = null,
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
    scoringParams = null,
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
    scoringParams = null,
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
    scoringParams = null,
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
    scoringParams = null,
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

smKitUI.startCustomizedWorkout(smWorkout, object : SMKitUIWorkoutListener {})
```