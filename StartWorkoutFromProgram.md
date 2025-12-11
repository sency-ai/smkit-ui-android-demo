# SMKitUI Android SDK: Workout From Program

This guide covers the full flow for running a Sency predefined workout program, including all options and result handling.

## Overview
Workout From Program lets you run Sency’s tailored workouts for specific clients. Contact Sency for your program ID.

## Prerequisites
- SDK configured (see README.md)
- CAMERA permission in AndroidManifest.xml

## Starting a Workout From Program
1. **Build WorkoutConfig** with your program details
2. **Start the workout**

### Example
```kotlin
val workoutConfig = WorkoutConfig(
    programId = "YOUR_PROGRAM_ID", // Provided by Sency
    week = 3,
    bodyZone = BodyZone.FullBody,
    levelType = DifficultyLevel.HighDifficulty,
    durationType = WorkoutDuration.Short,
    languageType = SMLanguage.Hebrew
)
smKitUI.startWorkoutProgram(workoutConfig, object : SMKitUIWorkoutListener {
    override fun handleWorkoutErrors(error: Error) { /* handle error */ }
    override fun workoutDidFinish(summary: WorkoutSummaryData) { /* handle summary */ }
    override fun didExitWorkout(summary: WorkoutSummaryData) { /* handle exit */ }
    override fun exerciseDidFinish(data: ExerciseData) { /* handle exercise end */ }
})
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

smKitUI.startWorkoutProgram(
    workoutConfig,
    listener = myListener,
    modifications = modifications
)
```

## Result Handling
- `workoutDidFinish(summary: WorkoutSummaryData)` — called when workout ends
- `didExitWorkout(summary: WorkoutSummaryData)` — called if user exits early
- See DataTypes.md for result structure

## Troubleshooting
- Always call `configure` before starting a workout
- Contact support@sency.ai for help
