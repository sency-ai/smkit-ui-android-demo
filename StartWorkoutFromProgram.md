# Start a workout from a program

This flow loads a Sency-authored workout from a program. Contact Sency for the program ID.

The SDK must be configured and camera permission granted before starting.

```kotlin
val workoutConfig = WorkoutConfig(
    programId = "YOUR_PROGRAM_ID",
    week = 3,
    bodyZone = BodyZone.FullBody,
    difficultyLevel = DifficultyLevel.HighDifficulty,
    workoutDuration = WorkoutDuration.Short,
    language = SMLanguage.English,
    shortIntro = true,
)

smKitUI.startWorkoutProgram(
    workoutConfig = workoutConfig,
    listener = object : SMKitUIWorkoutListener {
        override fun handleWorkoutErrors(error: Error) {
            // Display or report the error.
        }

        override fun workoutDidFinish(summary: WorkoutSummaryData) {
            // Persist the completed summary.
        }

        override fun didExitWorkout(summary: WorkoutSummaryData) {
            // The user exited before normal completion.
        }

        override fun exerciseDidFinish(data: ExerciseData) {
            // Observe each completed exercise.
        }
    },
    modifications = null,
    showPhoneCalibration = true,
)
```

`modifications` accepts the same feedback-threshold JSON supported by the assessment and custom-workout APIs. Keep it `null` unless Sency has supplied or reviewed the detector/feedback keys and thresholds.

The runnable version is the **Workout program** action in `MainActivity`; enter the program ID supplied by Sency before launching it.
