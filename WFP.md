# Workout From Program

> The Workout from Program enables you to run Sency's predefined workouts. those are tailored for specific clients. If you didn't contact us about your needs this page workout is not for you.

> A sample payload would look like this: [Payload](https://github.com/sency-ai/smkit-ui-react-native-demo/blob/main/Resources/program_summary_payload.json)

### Start Workout From Program

**Build Config** in order to starts a workout from program, you need configuration:

```kotlin
val workoutConfig = WorkoutConfig(
    programId = "YOUR_PROGRAM_ID", // String | Provided by Sency.
    week = 3, // Int | Week Number.  
    // Pick your preferred body zone to focus in the workout
    bodyZone = BodyZone.FullBody, // BodyZone | Enum class. 
    // Choose Difficulty of the workout
    levelType = DifficultyLevel.HighDifficulty, // DifficultyLevel | Enum class.
    // Choose Duration of the workout
    durationType = WorkoutDuration.Short, // Duration | Enum class.
    // Choose the language of the workout
    languageType = SMLanguage.Hebrew // SMLanguage | Enum class.
)
```

**startWorkout** starts a workout from program.

```kotlin
SMKitUI.startWorkoutProgram(workoutConfig, object : SMKitUIWorkoutListener {
    // Runtime error callback
    override fun handleWorkoutErrors(error: Error) {}

    // Workout session ended successfully callback
    override fun workoutDidFinish(summary: WorkoutSummaryData) {}

    // When user manually exit the workout callback
    override fun didExitWorkout(summary: WorkoutSummaryData) {}

    // When exercise is finished
    override fun exerciseDidFinish(data: ExerciseData) {}
})
```