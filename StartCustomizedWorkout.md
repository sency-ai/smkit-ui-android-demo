# SMKitUI Android SDK: Customized Workout

This guide covers the full flow for building and running a customized workout, including all options and result handling.

## Overview
Customized Workouts are similar to Customized Assessments, but do not use scoring and do not show a summary page.

## Prerequisites
- SDK configured (see README.md)
- CAMERA permission in AndroidManifest.xml

## Building a Customized Workout
1. **Create SMExercise objects** (with assets, summary info, but scoringParams = null)
2. **Create SMWorkout** (with your exercises and assets)
3. **Start the workout**

### Example
```kotlin
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
  // ... more exercises ...
)
val smWorkout = SMWorkout(
    id = "50",
    name = "demo workout",
    workoutIntro = "introduction",
    soundtrack = "soundtrack_7",
    exercises = exercises,
    workoutClosure = "workoutClosure.mp3",
    getInFrame = "bodycal_get_in_frame",
    bodycalFinished = "bodycal_finished"
)
smKitUI.startCustomizedWorkout(smWorkout, object : SMKitUIWorkoutListener {})
```

## Constraints
- `ScoringParams` must be **null** for each exercise
- No summary screen is shown

## Result Handling
- `workoutDidFinish(summary: WorkoutSummaryData)` — called when workout ends
- `didExitWorkout(summary: WorkoutSummaryData)` — called if user exits early
- See DataTypes.md for result structure

## Troubleshooting
- Always call `configure` before starting a workout
- Contact support@sency.ai for help
