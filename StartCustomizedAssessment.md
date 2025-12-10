# SMKitUI Android SDK: Customized Assessment

This guide covers the full flow for building and running a customized assessment, including all options and result handling.

## Overview
Customized Assessments let you create a personalized evaluation using exercises from Sency’s movement catalog. Scoring and summary are required.

## Prerequisites
- SDK configured (see README.md)
- CAMERA permission in AndroidManifest.xml

## Building a Customized Assessment
1. **Create SMExercise objects** (with assets, scoring, and summary info)
2. **Create SMWorkout** (with your exercises and assets)
3. **Start the assessment**

### Example
```kotlin
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
smKitUI.startCustomizedAssessment(smWorkout, showSummary = true, object : SMKitUIWorkoutListener {})
```

## 🔧 Modifying Feedback Parameters <a name="modify"></a>

You have the ability to modify specific feedback parameters for exercises.
This allows you to customize the thresholds and ranges for feedback detection.

To modify feedback parameters, use the following example:

```kotlin
val modifications = """
{
    "Crunches": {
        "CrunchesShallowDepth": {
            "low": 0.25,
            "high": 0.75
        }
    }
}
""".trimIndent()

smKitUI.startCustomizedAssessment(
    smWorkout,
    showSummary = true,
    listener = myListener,
    modifications = modifications
)
```

## Constraints
- `ScoringParams` is **mandatory** for each exercise
- Summary screen is included by default (can be toggled)
- See full constraints in CustomizedAssessment.md

## Result Handling
- `workoutDidFinish(summary: WorkoutSummaryData)` — called when assessment ends
- `didExitWorkout(summary: WorkoutSummaryData)` — called if user exits early
- See DataTypes.md for result structure

## Troubleshooting
- Always call `configure` before starting an assessment
- Contact support@sency.ai for help
